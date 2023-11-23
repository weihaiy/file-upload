package com.wei.util.fileupload.commons;

import com.github.tobato.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({FdfsClientConfig.class})
public class FastDFSConfig {

}