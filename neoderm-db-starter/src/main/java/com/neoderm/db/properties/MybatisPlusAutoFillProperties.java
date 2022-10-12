package com.neoderm.db.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(
        prefix = "mybatis-plus.auto-fill"
)
public class MybatisPlusAutoFillProperties {

    private Boolean enabled = true;

    private Boolean enableInsertFill = true;

    private Boolean enableUpdateFill = true;

    private String createTimeField = "createTime";

    private String updateTimeField = "updateTime";

    private String createByField = "createBy";

    private String updateByField = "updateBy";

}
