package javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainCallParser {

    /**
     * 链式调用信息容器
     */
    public static class ChainCallInfo {
        private final List<CallNode> callChain = new LinkedList<>();
        private final MethodCallExpr rootExpr;

        public ChainCallInfo(MethodCallExpr rootExpr) {
            this.rootExpr = rootExpr;
        }

        public void addNode(CallNode node) {
            callChain.add(node);
        }

        public List<CallNode> getCallChain() {
            return callChain;
        }

        public MethodCallExpr getRootExpr() {
            return rootExpr;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (CallNode node : callChain) {
                if (sb.length() > 0) {
                    sb.append(".");
                }
                sb.append(node.getMethodName())
                  .append("(")
                  .append(String.join(", ", node.getArgumentTypes()))
                  .append(")");
            }
            return sb.toString();
        }
    }

    /**
     * 调用节点信息
     */
    public static class CallNode {
        private final String methodName;
        private final List<String> argumentTypes;
        private final String callerType;

        public CallNode(String methodName, List<String> argumentTypes, String callerType) {
            this.methodName = methodName;
            this.argumentTypes = argumentTypes;
            this.callerType = callerType;
        }

        public String getMethodName() {
            return methodName;
        }

        public List<String> getArgumentTypes() {
            return argumentTypes;
        }

        public String getCallerType() {
            return callerType;
        }
    }

    /**
     * 链式调用访问器
     */
    private static class ChainCallVisitor extends VoidVisitorAdapter<List<ChainCallInfo>> {
        @Override
        public void visit(MethodCallExpr n, List<ChainCallInfo> chainCalls) {
            // 先处理子节点
            super.visit(n, chainCalls);

            // 检查是否是链式调用的根节点
            if (!(n.getParentNode().orElse(null) instanceof MethodCallExpr)) {
                ChainCallInfo chainInfo = new ChainCallInfo(n);
                buildCallChain(n, chainInfo);
                chainCalls.add(chainInfo);
            }
        }

        /**
         * 递归构建调用链
         */
        private void buildCallChain(MethodCallExpr callExpr, ChainCallInfo chainInfo) {
            // 解析当前节点信息
            CallNode node = parseCallNode(callExpr);
            chainInfo.addNode(node);

            // 继续向上查找链式调用
            callExpr.getScope().ifPresent(scope -> {
                if (scope instanceof MethodCallExpr) {
                    buildCallChain((MethodCallExpr) scope, chainInfo);
                }
            });
        }

        private CallNode parseCallNode(MethodCallExpr callExpr) {
            String methodName = callExpr.getNameAsString();
            List<String> argTypes = new ArrayList<>();

            // 增强的参数类型解析（带退避策略）
            callExpr.getArguments().forEach(arg -> {
                argTypes.add(safeResolveExpressionType(arg));
            });

            // 增强的调用者类型解析
            String callerType = callExpr.getScope()
                .map(scope -> {
                    // 如果是方法调用表达式，显示方法名
                    if (scope instanceof MethodCallExpr) {
                        return ((MethodCallExpr) scope).getNameAsString() + "()";
                    }
                    return safeResolveExpressionType(scope);
                })
                .orElse("<根对象>");

            return new CallNode(methodName, argTypes, callerType);
        }

        private String safeResolveExpressionType(Expression expr) {
            try {
                return expr.calculateResolvedType().describe();
            } catch (Exception e) {
                // 退避策略
                if (expr instanceof IntegerLiteralExpr) return "int";
                if (expr instanceof StringLiteralExpr) return "String";
                if (expr instanceof NameExpr) return ((NameExpr) expr).getNameAsString();
                if (expr instanceof FieldAccessExpr) return ((FieldAccessExpr) expr).getNameAsString();
                return expr.toString().replaceAll(".*", "");
            }
        }

        public String toDotGraph(ChainCallInfo chain) {
            StringBuilder dot = new StringBuilder();
            dot.append("digraph G {\n");
            dot.append("  rankdir=LR;\n");
            dot.append("  node [shape=box, style=rounded];\n\n");

            List<CallNode> nodes = chain.getCallChain();
            for (int i = 0; i < nodes.size(); i++) {
                CallNode node = nodes.get(i);
                dot.append(String.format("  node%d [label=\"%s\\n(%s)\"];\n",
                    i, node.getMethodName(), node.getCallerType()));

                if (i > 0) {
                    dot.append(String.format("  node%d -> node%d;\n", i-1, i));
                }
            }

            dot.append("}\n");
            return dot.toString();
        }

    /**
     * 解析Java文件中的链式调用
     */
    public List<ChainCallInfo> parseChainCalls(CompilationUnit cu) {
        List<ChainCallInfo> chainCalls = new ArrayList<>();
        ChainCallVisitor visitor = new ChainCallVisitor();
        cu.accept(visitor, chainCalls);
        
        // 反转链式调用顺序（从外到内改为从内到外）
        chainCalls.forEach(chain -> {
            List<CallNode> nodes = chain.getCallChain();
            for (int i = 0, j = nodes.size() - 1; i < j; i++) {
                nodes.add(i, nodes.remove(j));
            }
        });
        
        return chainCalls;
    }

    public static void main(String[] args) {
        String code = "public class Test {\n" +
                      "    void test() {\n" +
                      "        String result = new Builder()\n" +
                      "            .setName(\"John\")\n" +
                      "            .setAge(30)\n" +
                      "            .build();\n" +
                      "        \n" +
                      "        List<String> list = Stream.of(1,2,3)\n" +
                      "            .map(i -> i.toString())\n" +
                      "            .filter(s -> !s.isEmpty())\n" +
                      "            .collect(Collectors.toList());\n" +
                      "    }\n" +
                      "}";

        JavaParser javaParser = new JavaParser();
        CompilationUnit cu = javaParser.parse(code).getResult().get();
        ChainCallParser parser = new ChainCallParser();
        List<ChainCallInfo> chainCalls = parser.parseChainCalls(cu);

        System.out.println("找到链式调用: " + chainCalls.size());
        chainCalls.forEach(chain -> {
            System.out.println("\n完整调用链:");
            System.out.println(chain);
            
            System.out.println("\n详细节点信息:");
            List<CallNode> nodes = chain.getCallChain();
            for (int i = 0; i < nodes.size(); i++) {
                CallNode node = nodes.get(i);
                System.out.printf("%d. %s.%s(%s)%n",
                        i + 1,
                        node.getCallerType(),
                        node.getMethodName(),
                        String.join(", ", node.getArgumentTypes()));
            }
        });
    }
}