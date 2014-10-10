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
  
  <!-- Add provenance describing the changes -->
  <xsl:template match="eadheader/revisiondesc">
      <revisiondesc>
          <xsl:call-template name="add-change" />
          <xsl:apply-templates />
      </revisiondesc>
  </xsl:template>
  
  <xsl:template match="eadheader[not(revisiondesc)]">
      <eadheader><xsl:copy-of select="@*"/>
      <xsl:apply-templates />
      <revisiondesc>
          <xsl:call-template name="add-change" />
      </revisiondesc>
      </eadheader>
  </xsl:template>
  
  <xsl:template name="add-change">
    <xsl:variable name="convertdate" select="current-date()" />
    <change>
      <date><xsl:attribute name="normal" select="format-date($convertdate, '[Y0001]-[M01]-[D01]')" /></date>
      <item>If there were any, EAD formatting elements emph inside p elements have been converted to their Markdown equivalents by EHRI's preprocessing tool. 
      For example: &lt;emph render="italic"&gt;some text&lt;/emph&gt; becomes _some text_.</item>
    </change>
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
