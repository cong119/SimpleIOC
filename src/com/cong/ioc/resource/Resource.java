package com.cong.ioc.resource;

import java.io.IOException;
import java.io.InputStream;

// 读取资源文件
public interface Resource {

    InputStream getInputStream() throws IOException;

}
