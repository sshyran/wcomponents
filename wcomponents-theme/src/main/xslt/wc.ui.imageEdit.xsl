<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ui="https://github.com/bordertech/wcomponents/namespace/ui/v1.0" xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
	<xsl:import href="wc.constants.xsl"/>

	<!-- Currently has no direct UI artefact -->
	<xsl:template match="html:wc-imageedit"/>

	<!--
		Builds the a button that invokes an image editor.
	-->
	<xsl:template name="imageEditButton">
		<xsl:param name="text"/>
		<button type="button" data-wc-editor="{@editor}">
			<xsl:choose>
				<xsl:when test="self::ui:image">
					<xsl:attribute name="data-wc-selector">
						<xsl:value-of select="@editor"/>
					</xsl:attribute>
					<xsl:attribute name="data-wc-img">
						<xsl:value-of select="@id"/>
					</xsl:attribute>
					<xsl:attribute name="class">
						<xsl:text>wc_btn_icon wc-invite</xsl:text>
					</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="data-wc-selector">
						<xsl:value-of select="@id"/>
					</xsl:attribute>
					<xsl:attribute name="class">
						<xsl:text>wc_btn_camera wc_btn_icon wc-invite</xsl:text>
					</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<span class="wc-off">
				<xsl:value-of select="$text"/>
			</span>
		</button>
	</xsl:template>
</xsl:stylesheet>
