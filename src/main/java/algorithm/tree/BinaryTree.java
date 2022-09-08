package algorithm.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈前后中序遍历 指的是读取根的顺序〉<br>
 *
 * @author Carrie
 * @create 2022/1/29
 * @since 1.0.0
 */
public class BinaryTree<T extends Comparable<T>> {
    private static final Logger logger = LoggerFactory.getLogger(BinaryTree.class);
    private Node<T> root;

    /**
     * 二叉查找树 插入
     *
     * @param data
     * @return 是否插入成功
     */
    public boolean insert(T data) {
        if (null == root) {
            root = new Node<>(data);
            return true;
        }
        Node<T> current = root;
        while (true) {
            if (current.getData().compareTo(data) > 0) {
                Node<T> left = current.getLeft();
                if (null == left) {
                    current.setLeft(new Node(data));
                    break;
                }
                current = current.getLeft();
            }
            else {
                Node<T> right = current.getRight();
                if (null == right) {
                    current.setRight(new Node(data));
                    break;
                }
                current = current.getRight();
            }
        }
        return true;
    }

    /**
     * 查找第一个节点
     *
     * @param data
     * @return
     */
    public Node<T> contains(T data) {
        if (null == data || null == root) {
            return null;
        }
        Node<T> current = root;
        while (true) {
            if (current.getData().compareTo(data) > 0) {
                Node<T> left = current.getLeft();
                if (null == left) {
                    return null;
                }
                current = left;
                continue;
            }
            else if (current.getData().compareTo(data) < 0) {
                Node<T> right = current.getRight();
                if (null == right) {
                    return null;
                }
                current = right;
                continue;
            }
            return current;
        }
    }

    public void middleOrderTraversalRecursive(Node<T> node) {
        if (null == node) {
            return;
        }
        middleOrderTraversalRecursive(node.left);
        logger.info("node:{}", node.getData());
        middleOrderTraversalRecursive(node.right);
    }

    public void postOrderTraversalRecursive(Node<T> node) {
        if (null == node) {
            return;
        }
        postOrderTraversalRecursive(node.left);
        postOrderTraversalRecursive(node.right);
        logger.info("node:", node.getData());
    }

    public void preOrderNotRecursive(Node<T> node) {
        Stack<Node<T>> nodeStack = new Stack<>();
        while (null != node || !nodeStack.isEmpty()) {
            while (node != null) {
                logger.info("node:{}", node.getData());
                nodeStack.push(node);
                node = node.left;
            }
           if (!nodeStack.isEmpty()) {
               Node<T> parentNode = nodeStack.pop();
               node = parentNode.getRight();
           }
        }
    }

    public void preOrderNotRecursive1() {
        Stack<Node<T>> nodeStack = new Stack<>();
        if (null != root) {
            nodeStack.push(root);
            while (!nodeStack.isEmpty()) {
                Node<T> current = nodeStack.pop();
                logger.info("node:", current);
                if (null != current.getRight()) {
                    nodeStack.push(current.getRight());
                }
                if (null != current.getLeft()) {
                    nodeStack.push(current.getLeft());
                }
            }
        }
    }

    public void middleOrderNotRecursive() {
        Stack<Node<T>> nodeStack = new Stack<>();
        if (null != root) {
            Node<T> current = root;
            while (null != current || !nodeStack.isEmpty()) {
                if (current != null) {
                    nodeStack.push(current);
                    current = current.getLeft();
                }
                else {
                    Node<T> parentNode = nodeStack.pop();
                    logger.info("node:{}", parentNode.getData());
                    current = parentNode.getRight();
                }
            }
        }
    }

    public void postOrderNotRecursive() {
        Stack<Node<T>> nodeStackTemp = new Stack<>();
        Stack<Node<T>> nodeStack = new Stack<>();
        if (null != root) {
            nodeStackTemp.push(root);
            while (!nodeStackTemp.isEmpty()) {
                Node<T> parent = nodeStackTemp.pop();
                nodeStack.push(parent);
                if (null != parent.getLeft()) {
                    nodeStackTemp.push(parent.getLeft());
                }
                if (null != parent.getRight()) {
                    nodeStackTemp.push(parent.getRight());
                }
            }
            while (!nodeStack.isEmpty()) {
               logger.info("node:{}", nodeStack.pop().getData());
            }
        }
    }

    public void preOrderTraversalRecursive(Node<T> node) {
        if (null == node) {
            return;
        }
        logger.info("node:{}", node.getData());
        preOrderTraversalRecursive(node.left);
        preOrderTraversalRecursive(node.right);
    }

    public void levelOrderTraversalRecursive() {
        Queue<Node<T>> queue = new LinkedList<>();
        if (null != root) {
            queue.offer(root);
            while (!queue.isEmpty()) {
                Node<T> current = queue.remove();
                logger.info("node:{}", current.getData());
                if (null != current.getLeft()) {
                    queue.offer(current.getLeft());
                }
                if (null != current.getRight()) {
                    queue.offer(current.getRight());
                }
            }
        }
    }

    public void levelOrderTraversalNotRecursive(Node node) {
        if (null == node) {
            return;
        }
        int level = treeDepth(node);
        if (level > 0) {
            for (int i = 1; i <= level; i++) {
                levelOrderTraversalRecursive(node, level);
            }
        }
    }

    private void levelOrderTraversalRecursive(Node node, int level) {
        if (null == node || level < 1) {
            return;
        }
        if (level == 1) {
            logger.info("node:{}", node.getData());
            return;
        }
        levelOrderTraversalRecursive(node.getLeft(), level - 1);
        levelOrderTraversalRecursive(node.getRight(), level - 1);
    }

    public int treeDepth(Node node) {
        if (null == node) {
            return 0;
        }
        int l = treeDepth(node.getLeft());
        int r = treeDepth(node.getRight());
        if (l > r) {
            return l + 1;
        }
        else {
            return r + 1;
        }
    }

    public static void main(String[] args) {
        BinaryTree<NodeData> binaryTree = new BinaryTree<>();
        binaryTree.insert(new NodeData(2));
        binaryTree.insert(new NodeData(3));
        binaryTree.insert(new NodeData(4));
        binaryTree.insert(new NodeData(1));
        binaryTree.postOrderNotRecursive();
        logger.debug(binaryTree.contains(new NodeData(3)).toString());
    }

    static class Node<T> {
        private T data;
        private Node left;
        private Node right;

        public Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        @Override
        public String toString() {
            return "Node{" +
                "data=" + data +
                ", left=" + left +
                ", right=" + right +
                '}';
        }
    }

    static class NodeData implements Comparable<NodeData>{
        private int i = 0;

        public NodeData(int i) {
            this.i = i;
        }

        @Override
        public int compareTo(NodeData o) {
            if (i == o.getI()) {
                return 0;
            }
            return i - o.getI() > 0 ? 1 : -1;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        @Override
        public String toString() {
            return "NodeData{" +
                "i=" + i +
                '}';
        }
    }
}