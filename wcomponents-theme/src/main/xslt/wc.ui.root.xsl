<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ui="https://github.com/bordertech/wcomponents/namespace/ui/v1.0" xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
	<xsl:import href="wc.constants.xsl"/>
	<xsl:import href="wc.ui.root.n.addHeadMetaBeforeTitle.xsl"/>
	<xsl:import href="wc.ui.root.n.makeIE8CompatScripts.xsl"/>
	<xsl:import href="wc.ui.root.n.makeRequireConfig.xsl"/>
	<xsl:import href="wc.ui.root.n.externalScript.xsl"/>
	<xsl:import href="wc.common.registrationScripts.xsl"/>
	<xsl:import href="wc.ui.root.n.wcBodyClass.xsl"/>
	<xsl:import href="wc.ui.root.n.webAnalytics.xsl"/>
	<!--
		Some meta elements have to be VERY early to work reliably. Put them here.

		NOTE: If you need the old XSLT which enabled a WComponent application to be nested inside an existing HTML
		structure you will need to either rewrite it or retrieve it from the archives. It has gone because it was slow
		and no-one needs it anymore.
	-->
	<xsl:strip-space elements="*"/>
	<xsl:template match="ui:root">
		<html>
			<xsl:if test="@lang">
				<xsl:attribute name="lang">
					<xsl:value-of select="@lang"/>
				</xsl:attribute>
			</xsl:if>
			<head>
				<!-- Works more reliably if it is first -->
				<xsl:call-template name="includeFavicon"/>
				<!--
					The format-detection is needed to work around issues in some very popular mobile browsers that will convert
					"numbers" into phone links (a elements) if they appear to be phone numbers, even if those numbers are the
					content of buttons or links. This breaks important stuff if you, for example, want to link or submit using
					a number identifier.
					
					If you want a phone number link in these (or any) browser use WPhoneNumberField set readOnly.
				-->
				<xsl:element name="meta">
					<xsl:attribute name="name">
						<xsl:text>format-detection</xsl:text>
					</xsl:attribute>
					<xsl:attribute name="content">
						<xsl:text>telephone=no</xsl:text>
					</xsl:attribute>
				</xsl:element>
				<xsl:element name="meta">
					<xsl:attribute name="name"><xsl:text>viewport</xsl:text></xsl:attribute>
					<xsl:attribute name="content"><xsl:text>initial-scale=1</xsl:text></xsl:attribute>
				</xsl:element>
				<xsl:call-template name="addHeadMetaBeforeTitle"/>
				<title>
					<xsl:value-of select="@title"/>
				</title>

				<link type="text/css" id="wc_css_screen" rel="stylesheet"><!-- this id is used by the style loader js -->
					<xsl:attribute name="href">
						<xsl:value-of select="$cssFilePath"/>
					</xsl:attribute>
				</link>

				<xsl:apply-templates select="ui:application/ui:css" mode="inHead"/>
				<xsl:apply-templates select=".//html:link[@rel='stylesheet']" mode="inHead"/>

				<!--
					We need to set up the require config very early.
				-->
				<xsl:call-template name="makeRequireConfig"/>

				<!--
					non-AMD compatible fixes for IE: things that need to be fixed before we can require anything but
					have to be added after we have included requirejs/require.
				-->
				<xsl:call-template name="makeIE8CompatScripts"/>

				<xsl:call-template name="externalScript">
					<xsl:with-param name="scriptName" select="'lib/require'"/>
				</xsl:call-template>

				<!-- We can delete some script nodes after they have been used. To do this we need the script element to have an ID. -->
				<xsl:variable name="scriptId" select="generate-id()"/>
				<!-- We want to load up the CSS as soon as we can, so do it immediately after loading require. -->
				<xsl:variable name="styleLoaderId" select="concat($scriptId,'-styleloader')"/>
				<script type="text/javascript" id="{$styleLoaderId}">
					<xsl:text>require(["wc/compat/compat!"], function() {</xsl:text>
					<xsl:text>require(["wc/a8n", "wc/loader/style", "wc/dom/removeElement"</xsl:text>
					<xsl:if test="$isDebug=1">
						<xsl:text>,"wc/debug/common"</xsl:text>
					</xsl:if>
					<xsl:text>], function(a, s, r){try{s.load();}finally{r("</xsl:text>
					<xsl:value-of select="$styleLoaderId"/>
					<xsl:text>", 250);}});</xsl:text>
					<xsl:text>});</xsl:text>
				</script>

				<xsl:call-template name="registrationScripts"/>
				<!--
					We grab all base, meta and link elements from the content and place
					them in the head where they belong.
				-->
				<xsl:apply-templates select="ui:application/ui:js" mode="inHead"/>
				<xsl:apply-templates select=".//html:base|.//html:link[not(@rel='icon' or @rel='shortcut icon' or @rel='stylesheet')]|.//html:meta" mode="inHead"/>
				<xsl:call-template name="webAnalytics"/>
			</head>
			<xsl:variable name="bodyClass">
				<xsl:call-template name="wcBodyClass"/>
			</xsl:variable>
			<body>
				<xsl:attribute name="data-wc-domready">
					<xsl:text>false</xsl:text><!-- JS will set this to true - this is for automation testing tools -->
				</xsl:attribute>
				<xsl:if test="$bodyClass!=''">
					<xsl:attribute name="class">
						<xsl:value-of select="normalize-space($bodyClass)"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="$isDebug=1">
					<xsl:comment>
						XSLT processor: <xsl:value-of select="system-property('xsl:vendor')"/>
						base-uri available? <xsl:value-of select="function-available('base-uri')"/>
					</xsl:comment>
				</xsl:if>
				<!--
					loading indicator and shim
					We show a loading indicator as we construct the page then remove it as part of post-initialisation.
					This helps to prevent users interacting with a page before it is ready. The modal shim is part of
					the page level loading indicator.
				-->
				<div id="wc-shim" class="wc_shim_loading">
					<xsl:text>&#xa0;</xsl:text>
					<noscript>
						<p>
							<xsl:text>You must have JavaScript enabled to use this application.</xsl:text>
						</p>
					</noscript>
				</div>
				<div id="wc-ui-loading">
					<div tabindex="0" class="wc-icon">&#x200b;</div>
				</div>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
