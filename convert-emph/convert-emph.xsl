<?xml version='1.0'?>
<!--
//*****************************************************************************
// Pre-process EAD files to convert <emph render="italic"> to MarkDown.
//
// Distributed under the GNU General Public Licence
//*****************************************************************************
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="2.0">
  <xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>
  <xsl:output encoding="UTF-8"/>


  <xsl:template match="/">
      
        <xsl:apply-templates />
      
  </xsl:template>
  
  
  <xsl:template match="emph[parent::p][@render='italic']">
      <xsl:value-of select="concat('_', ./normalize-space(), '_')" />
  </xsl:template>
  
  <xsl:template match="emph[parent::p][@render='bold']">
      <xsl:value-of select="concat('**', ./normalize-space(), '**')" />
  </xsl:template>
  
  <xsl:template match="node()|@*">
    <xsl:copy>
        <xsl:apply-templates select="node()|@*" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
