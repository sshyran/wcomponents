<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:ui="https://github.com/bordertech/wcomponents/namespace/ui/v1.0" 
	xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
	<!--
		Outputs aria-controls and data-wc-ajaxalias attributes for any AJAX enabled table control: sort, pagination, 
		rowExpansion. This should not be overridden.

		param tableId: The id attribute of the nearest ancestor ui:table.

		Structural: do not override.
	-->
	<xsl:template name="tableAjaxController">
		<xsl:param name="tableId"/>
		<xsl:attribute name="aria-controls">
			<xsl:value-of select="$tableId"/>
		</xsl:attribute>
		<xsl:attribute name="data-wc-ajaxalias">
			<xsl:value-of select="$tableId"/>
		</xsl:attribute>
	</xsl:template>
</xsl:stylesheet>
