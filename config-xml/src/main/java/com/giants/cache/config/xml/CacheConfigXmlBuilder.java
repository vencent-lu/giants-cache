package com.giants.cache.config.xml;

import com.giants.cache.common.CacheConstants;
import com.giants.cache.config.CacheConfig;
import com.giants.cache.config.CacheConfigBuilder;
import com.giants.xmlmapping.XmlDataModule;
import com.giants.xmlmapping.XmlMappingData;
import com.giants.xmlmapping.config.exception.XmlMapException;
import com.giants.xmlmapping.exception.XMLParseException;
import com.giants.xmlmapping.exception.XmlDataException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheConfigXmlBuilder 缓存配置构建器
 * date time: 2024/12/16 16:03
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class CacheConfigXmlBuilder implements CacheConfigBuilder {

    protected static final Logger logger = LoggerFactory.getLogger(CacheConfigXmlBuilder.class);

    private final String cacheConfigKey;
    private final String cacheConfigXmlFilePath;

    public CacheConfigXmlBuilder() {
        this.cacheConfigKey = CacheConstants.DEFAULT_CONFIG_KEY;
        this.cacheConfigXmlFilePath = CacheConstants.DEFAULT_CONFIG_FILE_PATH;
    }

    public CacheConfigXmlBuilder(String cacheConfigKey, String cacheConfigXmlFilePath) {
        this.cacheConfigKey = cacheConfigKey;
        this.cacheConfigXmlFilePath = cacheConfigXmlFilePath;
    }

    @Override
    public String getCacheConfigKey() {
        return this.cacheConfigKey;
    }

    @Override
    public CacheConfig build() {
        if (StringUtils.isNotEmpty(this.cacheConfigXmlFilePath)) {
            try {
                XmlMappingData giantsCacheConfigMappingData = new XmlMappingData(CacheConfigXml.class);
                giantsCacheConfigMappingData.loadXmls(this.cacheConfigXmlFilePath);
                XmlDataModule<CacheConfigXml> xmlDataModule =
                        giantsCacheConfigMappingData.getDataModule(CacheConfigXml.class);
                if (xmlDataModule != null && xmlDataModule.isNotEmpty()) {
                    return xmlDataModule.getAll().iterator().next();
                }
            } catch (XmlMapException | XmlDataException | XMLParseException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }
}
