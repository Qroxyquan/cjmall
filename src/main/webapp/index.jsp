<%@page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8" %>
<html>
<body>
<h2>Hello World!</h2>
mvc上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">

    <input type="file" name="upload_file">
    <input type="submit" value="spring-MVc上传文件">

</form>
mvc富文本上传文件
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">

    <input type="file" name="upload_file">
    <input type="submit" value="富文本上传文件">

</form>
</body>
</html>
