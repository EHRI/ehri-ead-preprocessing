<?xml version='1.0'?>
<!--
//*****************************************************************************
// Pre-process EAD files from the Wiener Library to join values from <emph> 
// elements within access points.
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
      <item>If there were any emph elements inside elements other than p, the emph tags have been stripped by EHRI's preprocessing tool.
      For example, &lt;emph altrender="firstname"&gt;Ben&lt;/emph&gt; became Ben. Multiple occurrences have been padded with commas. 
      Empty subject elements have been removed too.</item>
    </change>
  </xsl:template>
  
  <xsl:template match="emph[parent::*[name() != 'p']]">
      <xsl:value-of select="./normalize-space()" />
      <xsl:if test="position()!=last()">, </xsl:if>
  </xsl:template>
  
  <!-- remove empty subject -->
  <xsl:template match="subject[not(node())]"/>
  
  <xsl:template match="node()|@*">
    <xsl:copy>
        <xsl:apply-templates select="node()|@*" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
