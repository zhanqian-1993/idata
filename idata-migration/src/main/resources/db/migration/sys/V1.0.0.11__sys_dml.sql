update idata.sys_config set value_one = '{"url":{"configValue":""},"token":{"configValue":""},"prodDSProjectCode":{"configValue":""},"stagDSProjectCode":{"configValue":""},"DStenantCode":{"configValue":""},"DSWorkGroup":{"configValue":""},"dagTimeout":{"configValue":""}}'
where key_one = 'ds-config';
update idata.sys_config set value_one = '{"yarn.addr":{"configValue":""},"kylin.auth":{"configValue":""},"cluster":{"configValue":""},"idata.insert.erase.url":{"configValue":""},"idata.job.detail.url":{"configValue":""},"idata.sql.rewrite.url":{"configValue":""},"hdfs.addr":{"configValue":""},"kylin.api.url":{"configValue":""},"idata.monitor.url":{"configValue":""}}'
where key_one = 'htool-config';