package com.demo.config

import java.util.*

class PropertiesConfig private constructor() {
    companion object {
        private const val CONFIG_FILE_PATH = "github.properties"
        private val configFileStream = PropertiesConfig::class.java.classLoader.getResourceAsStream(CONFIG_FILE_PATH)

        private val properties: Properties by lazy {
            loadProperties()
        }

        /**
         * Loads the configuration file and returns the Properties object that holds the properties
         */
        private fun loadProperties(): Properties {
            val props = Properties()
            props.load(configFileStream)
            return props
        }

        /**
         * Gets the value of the property with the given key
         *
         * @param key the key of the property
         */
        fun get(key: String): String = properties.getProperty(key)
    }
}