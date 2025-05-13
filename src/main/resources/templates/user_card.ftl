<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户信息卡片</title>
    <style>
        .premium { color: gold; font-weight: bold; }
        .regular { color: gray; }
    </style>
</head>
<body>
<div class="user-card">
    <h1>欢迎，<#if gender == "male">${userName} 先生<#elseif gender == "female">${userName} 女士<#else>${userName}</#if>！</h1>

    <p>年龄：${age}</p>

    <p>
        <#if isPremium>
            <span class="premium">高级会员</span>
        <#else>
            <span class="regular">普通会员</span>
        </#if>
    </p>

    <p>
        <#if age < 18>
            <strong>注意：未成年用户</strong>
        <#else>
            <strong>成年用户</strong>
        </#if>
    </p>

    <#if aa?has_content>
        Name is available: ${aa}
    <#else>
        Name is not provided.
    </#if>
    <#if bb?has_content>
        Name is available: ${aa}
    <#else>
        Name is not provided.
    </#if>
</div>
</body>
</html>
