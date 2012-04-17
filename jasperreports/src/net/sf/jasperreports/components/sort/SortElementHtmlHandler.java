/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.components.sort;

import java.awt.Color;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.sf.jasperreports.components.BaseElementHtmlHandler;
import net.sf.jasperreports.components.headertoolbar.HeaderToolbarElement;
import net.sf.jasperreports.components.sort.actions.SortAction;
import net.sf.jasperreports.components.sort.actions.SortData;
import net.sf.jasperreports.engine.CompositeDatasetFilter;
import net.sf.jasperreports.engine.DatasetFilter;
import net.sf.jasperreports.engine.JRGenericPrintElement;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;
import net.sf.jasperreports.engine.JRPrintHyperlinkParameters;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.ReportContext;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBasePrintHyperlink;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRHtmlExporterContext;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.type.JREnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRColorUtil;
import net.sf.jasperreports.engine.util.MessageProvider;
import net.sf.jasperreports.engine.util.MessageUtil;
import net.sf.jasperreports.repo.JasperDesignCache;
import net.sf.jasperreports.web.WebReportContext;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.servlets.ReportServlet;
import net.sf.jasperreports.web.servlets.ResourceServlet;
import net.sf.jasperreports.web.util.ReportExecutionHyperlinkProducer;
import net.sf.jasperreports.web.util.VelocityUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id:ChartThemesUtilities.java 2595 2009-02-10 17:56:51Z teodord $
 */
public class SortElementHtmlHandler extends BaseElementHtmlHandler
{
	private static final Log log = LogFactory.getLog(SortElementHtmlHandler.class);
	
	private static final String RESOURCE_SORT_JS = "net/sf/jasperreports/components/sort/resources/sort.js";
	private static final String RESOURCE_IMAGE_CLOSE = "net/sf/jasperreports/components/sort/resources/images/delete_edit.gif";
	private static final String RESOURCE_TRANSPARENT_PIXEL = "net/sf/jasperreports/engine/images/pixel.GIF";
	
	private static final String RESOURCE_FILTER_SYMBOL = "net/sf/jasperreports/components/sort/resources/images/filter.png";
	private static final String RESOURCE_WRONG_FILTER_SYMBOL = "net/sf/jasperreports/components/sort/resources/images/filter_wrong.png";
	private static final String RESOURCE_SORT_SYMBOL_ASC = "net/sf/jasperreports/components/sort/resources/images/sort_asc.png";
	private static final String RESOURCE_SORT_SYMBOL_ASC_HOVER = "net/sf/jasperreports/components/sort/resources/images/sort_asc_over.png";
	private static final String RESOURCE_SORT_SYMBOL_DESC = "net/sf/jasperreports/components/sort/resources/images/sort_desc.png";
	private static final String RESOURCE_SORT_SYMBOL_DESC_OVER = "net/sf/jasperreports/components/sort/resources/images/sort_desc_over.png";
	
	private static final String SORT_ELEMENT_HTML_TEMPLATE = "net/sf/jasperreports/components/sort/resources/SortElementHtmlTemplate.vm";
	
	protected static final String HTML_VERTICAL_ALIGN_TOP = "top";
	protected static final String CSS_TEXT_ALIGN_LEFT = "left";
	protected static final String SORT_SYMBOL_ASCENDING = "&#9650;";
	protected static final String SORT_SYMBOL_DESCENDING = "&#9660;";
	
	protected static final String FILTER_SYMBOL_ACTIVE = "Active";
	protected static final String FILTER_SYMBOL_INACTIVE = "Inactive";

	public String getHtmlFragment(JRHtmlExporterContext context, JRGenericPrintElement element)
	{
		String htmlFragment = null;
		ReportContext reportContext = context.getExporter().getReportContext();
		if (reportContext != null)//FIXMEJIVE
		{
			String sortColumnName = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_NAME);
			String sortColumnLabel = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_LABEL);
			String sortColumnType = (String) element.getParameterValue(SortElement.PARAMETER_SORT_COLUMN_TYPE);
			
			String sortHandlerVAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_VERTICAL_ALIGN);
			String sortHandlerHAlign = (String) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_HORIZONTAL_ALIGN);
			String sortDatasetName = element.getPropertiesMap().getProperty(SortElement.PROPERTY_DATASET_RUN);
			
			JRBaseFont sortHandlerFont = (JRBaseFont) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_FONT);
			if (sortHandlerFont == null) {
				sortHandlerFont = new JRBaseFont(element);
			}

			Color sortHandlerColor = (Color) element.getParameterValue(SortElement.PARAMETER_SORT_HANDLER_COLOR);
			if (sortHandlerColor == null) {
				sortHandlerColor = Color.WHITE;
			}
			
			FilterTypesEnum filterType = FilterTypesEnum.getByName(element.getPropertiesMap().getProperty(SortElement.PROPERTY_FILTER_TYPE));
			if (filterType == null)//FIXMEJIVE
			{
				return null;
			}
			
			String filterPattern = element.getPropertiesMap().getProperty(SortElement.PROPERTY_FILTER_PATTERN);

			Locale locale = (Locale) reportContext.getParameterValue(JRParameter.REPORT_LOCALE);
			
			if (log.isDebugEnabled()) {
				log.debug("report locale: " + locale);
			}
			
			if (locale == null) {
				locale = Locale.getDefault();
			}
			
			Map<String, String> translatedOperators = null;
			switch (filterType) {
				case NUMERIC:
					translatedOperators = getTranslatedOperators(context.getJasperReportsContext(), FilterTypeNumericOperatorsEnum.class.getName(), FilterTypeNumericOperatorsEnum.values(), locale);
					break;
				case DATE:
					translatedOperators = getTranslatedOperators(context.getJasperReportsContext(), FilterTypeDateOperatorsEnum.class.getName(), FilterTypeDateOperatorsEnum.values(), locale);
					break;
				case TEXT:
					translatedOperators = getTranslatedOperators(context.getJasperReportsContext(), FilterTypeTextOperatorsEnum.class.getName(), FilterTypeTextOperatorsEnum.values(), locale);
					break;
			}
			
			String appContextPath = (String)reportContext.getParameterValue("net.sf.jasperreports.web.app.context.path");//FIXMEJIVE define constant
			
			VelocityContext velocityContext = new VelocityContext();
			String webResourcesBasePath = JRPropertiesUtil.getInstance(context.getJasperReportsContext()).getProperty("net.sf.jasperreports.web.resources.base.path");
			if (webResourcesBasePath == null)
			{
				webResourcesBasePath = ResourceServlet.DEFAULT_PATH + "?" + ResourceServlet.RESOURCE_URI + "=";
			}
			String imagesResourcePath = (appContextPath == null ? "" : appContextPath) + webResourcesBasePath;//FIXMEJIVE

			velocityContext.put("resourceSortJs", webResourcesBasePath + SortElementHtmlHandler.RESOURCE_SORT_JS);
			velocityContext.put("elementX", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getX()));
			velocityContext.put("elementY", ((JRXhtmlExporter)context.getExporter()).toSizeUnit(element.getY()));
			velocityContext.put("elementWidth", element.getWidth());
			velocityContext.put("elementHeight", element.getHeight());
			velocityContext.put("sortLinkClass", sortDatasetName);
			velocityContext.put("sortHandlerHAlign", sortHandlerHAlign != null ? sortHandlerHAlign : CSS_TEXT_ALIGN_LEFT);
			velocityContext.put("sortHandlerVAlign", sortHandlerVAlign != null ? sortHandlerVAlign : HTML_VERTICAL_ALIGN_TOP);
			velocityContext.put("sortHandlerColor", JRColorUtil.getColorHexa(sortHandlerColor));
			velocityContext.put("sortHandlerFontSize", sortHandlerFont.getFontSize());
			velocityContext.put("transparentPixelSrc", imagesResourcePath + SortElementHtmlHandler.RESOURCE_TRANSPARENT_PIXEL);
			
			velocityContext.put("isFilterable", filterType != null);
			velocityContext.put("filterDivId", "filter_" + sortDatasetName + "_" + sortColumnName);
			velocityContext.put("filterFormAction", getFilterFormActionLink(context));
			velocityContext.put("filterReportUriParamName", ReportServlet.REQUEST_PARAMETER_REPORT_URI);
			velocityContext.put("filterReportUriParamValue", reportContext.getParameterValue(ReportServlet.REQUEST_PARAMETER_REPORT_URI));
			velocityContext.put("filterFieldParamName", SortElement.REQUEST_PARAMETER_FILTER_FIELD);
			velocityContext.put("filterColumnName", sortColumnName);
			velocityContext.put("filterColumnNameLabel", sortColumnLabel != null ? sortColumnLabel : "");
			velocityContext.put("filterTableNameParam", SortElement.REQUEST_PARAMETER_DATASET_RUN);
			velocityContext.put("filterTableNameValue", sortDatasetName);
			velocityContext.put("filterCloseDialogImageResource", imagesResourcePath + SortElementHtmlHandler.RESOURCE_IMAGE_CLOSE);

			velocityContext.put("filterTypeParamName", SortElement.REQUEST_PARAMETER_FILTER_TYPE);
			velocityContext.put("filterTypeParamNameValue", filterType.getName());
			velocityContext.put("filterTypeOperatorParamName", SortElement.REQUEST_PARAMETER_FILTER_TYPE_OPERATOR);
			
			velocityContext.put("filterPatternParamName", SortElement.REQUEST_PARAMETER_FILTER_PATTERN);
			velocityContext.put("filterPatternParamValue", filterPattern);

			velocityContext.put("filterTypeValuesMap", translatedOperators);
			
			velocityContext.put("filterValueStartParamName", SortElement.REQUEST_PARAMETER_FILTER_VALUE_START);
			velocityContext.put("filterValueEndParamName", SortElement.REQUEST_PARAMETER_FILTER_VALUE_END);
			
			
			
			if (element.getModeValue() == ModeEnum.OPAQUE)
			{
				velocityContext.put("backgroundColor", JRColorUtil.getColorHexa(element.getBackcolor()));
			}

			String sortField = getCurrentSortField(context.getJasperReportsContext(), reportContext, element.getUUID().toString(), sortDatasetName, sortColumnName, sortColumnType);
			if (sortField == null) 
			{
				SortData sortData = new SortData(element.getUUID().toString(), sortColumnName, sortColumnType, SortElement.SORT_ORDER_ASC);
				//velocityContext.put("sortHref", getSortLink(context, sortColumnName, sortColumnType, SortElement.SORT_ORDER_ASC, sortDatasetName));
				velocityContext.put("sortHref", getSortLink(context, sortData));
				velocityContext.put("isSorted", false);
			}
			else 
			{
				String[] sortActionData = SortElementUtils.extractColumnInfo(sortField);
				boolean isAscending = SortElement.SORT_ORDER_ASC.equals(sortActionData[2]);
				String sortOrder = !isAscending ? SortElement.SORT_ORDER_NONE : SortElement.SORT_ORDER_DESC;
				SortData sortData = new SortData(element.getUUID().toString(), sortColumnName, sortColumnType, sortOrder);
				//velocityContext.put("sortHref", getSortLink(context, sortColumnName, sortColumnType, sortOrder, sortDatasetName));
				velocityContext.put("sortHref", getSortLink(context, sortData));
				velocityContext.put("isSorted", true);
				velocityContext.put("sortSymbolResource", isAscending ? imagesResourcePath + RESOURCE_SORT_SYMBOL_ASC : imagesResourcePath + RESOURCE_SORT_SYMBOL_DESC);
				velocityContext.put("sortSymbolHoverResource", isAscending ? imagesResourcePath + RESOURCE_SORT_SYMBOL_ASC_HOVER : imagesResourcePath + RESOURCE_SORT_SYMBOL_DESC_OVER);
			}
			
			// existing filters
			String currentDataset = (String) reportContext.getParameterValue(SortElement.REQUEST_PARAMETER_DATASET_RUN);
			String filterValueStart = "";
			String filterValueEnd = "";
			String filterTypeOperatorValue = "";
			String filterActiveInactive = FILTER_SYMBOL_INACTIVE;
			boolean isFiltered = false;
			boolean enableFilterEndParameter = false;
			List<FieldFilter> fieldFilters = new ArrayList<FieldFilter>();
			String filterSymbolImageResource = imagesResourcePath + SortElementHtmlHandler.RESOURCE_FILTER_SYMBOL;

			if (sortDatasetName != null && sortDatasetName.equals(currentDataset))
			{
				String currentTableFiltersParam = currentDataset + SortElement.FILTER_FIELDS_PARAM_SUFFIX;
				DatasetFilter existingFilter = (DatasetFilter) reportContext.getParameterValue(currentTableFiltersParam);
				getFieldFilters(existingFilter, fieldFilters, sortColumnName);
				
				if (fieldFilters.size() > 0) {
					FieldFilter ff = fieldFilters.get(0);
					if (ff.getFilterValueStart() != null) {
						filterValueStart = ff.getFilterValueStart();
					}
					if (ff.getFilterValueEnd() != null) {
						filterValueEnd = ff.getFilterValueEnd();
					}
					filterTypeOperatorValue = ff.getFilterTypeOperator();
					filterActiveInactive = FILTER_SYMBOL_ACTIVE;
					isFiltered = true;
					if (filterTypeOperatorValue != null && filterTypeOperatorValue.toLowerCase().contains("between")) {
						enableFilterEndParameter = true;
					}
					if (!ff.getIsValid()) {
						filterSymbolImageResource = imagesResourcePath + SortElementHtmlHandler.RESOURCE_WRONG_FILTER_SYMBOL;
					}
					
				}
			}
			
			velocityContext.put("isFiltered", isFiltered);
			velocityContext.put("filterSymbolImageResource", filterSymbolImageResource);
			velocityContext.put("filterToRemoveParamName", SortElement.REQUEST_PARAMETER_REMOVE_FILTER);
			velocityContext.put("filterToRemoveParamvalue", sortColumnName);
			
			String filtersJsonString = getJsonString(fieldFilters).replaceAll("\\\"", "\\\\\\\"");
			if (log.isDebugEnabled()) {
				log.debug("filtersJsonString: " + filtersJsonString);
			}
			velocityContext.put("filtersJsonString", filtersJsonString);

			velocityContext.put("filterValueStartParamValue", filterValueStart);
			velocityContext.put("filterValueEndParamValue", filterValueEnd);
			velocityContext.put("filterTypeOperatorParamValue", filterTypeOperatorValue);
			velocityContext.put("filterActiveInactive", filterActiveInactive);
			velocityContext.put("enableFilterEndParameter", enableFilterEndParameter);
			
			htmlFragment = VelocityUtil.processTemplate(SortElementHtmlHandler.SORT_ELEMENT_HTML_TEMPLATE, velocityContext);
		}
		
		return htmlFragment;
	}
	
	/**
	 * 
	 *
	private String getSortLink(JRHtmlExporterContext context, String sortColumnName, String sortColumnType, String sortOrder, String sortTableName) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType(ReportExecutionHyperlinkProducer.HYPERLINK_TYPE_REPORT_EXECUTION);
		
		JRPrintHyperlinkParameters parameters = new JRPrintHyperlinkParameters();
//		parameters.addParameter(
//			new JRPrintHyperlinkParameter(
//				SortElement.REQUEST_PARAMETER_SORT_DATA,
//				String.class.getName(), 
//				SortElementUtils.packSortColumnInfo(sortColumnName, sortColumnType, sortOrder))
//				);
//		parameters.addParameter(new JRPrintHyperlinkParameter(SortElement.REQUEST_PARAMETER_DATASET_RUN, String.class.getName(), sortTableName));
		parameters.addParameter(
			new JRPrintHyperlinkParameter(
				ReportServlet.REQUEST_PARAMETER_ACTION, 
				String.class.getName(), 
				"{\"actionName\":\"sortica\",\"sortData\":{\"uuid\":\"8b9779e9-6eb5-4b3f-a03b-3db4633e8e89\"," +
				"\"sortColumnName\":\"" + sortColumnName + "\"," +
				"\"sortColumnType\":\"" + sortColumnType + "\"," +
				"\"sortOrder\":\"" + sortOrder + "\"" +
				//",\"sortDatasetName\":\"TableReport_TableDataset_1327925074017_135700\"" +
				"}}"
				)
			);
		
		ReportContext reportContext = context.getExporter().getReportContext();
		parameters.addParameter(new JRPrintHyperlinkParameter(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID, String.class.getName(), reportContext.getId()));
		parameters.addParameter(new JRPrintHyperlinkParameter(ReportServlet.REQUEST_PARAMETER_RUN_REPORT, String.class.getName(), "true"));
		
		hyperlink.setHyperlinkParameters(parameters);
		
		return context.getHyperlinkURL(hyperlink);
	}
	*/
	
	private String getSortLink(JRHtmlExporterContext context, SortData sortData) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType(ReportExecutionHyperlinkProducer.HYPERLINK_TYPE_REPORT_EXECUTION);
		
		JRPrintHyperlinkParameters parameters = new JRPrintHyperlinkParameters();
//		parameters.addParameter(
//			new JRPrintHyperlinkParameter(
//				SortElement.REQUEST_PARAMETER_SORT_DATA,
//				String.class.getName(), 
//				SortElementUtils.packSortColumnInfo(sortColumnName, sortColumnType, sortOrder))
//				);
//		parameters.addParameter(new JRPrintHyperlinkParameter(SortElement.REQUEST_PARAMETER_DATASET_RUN, String.class.getName(), sortTableName));
		parameters.addParameter(
			new JRPrintHyperlinkParameter(
				ReportServlet.REQUEST_PARAMETER_ACTION, 
				String.class.getName(), 
				"{\"actionName\":\"sortica\",\"sortData\":{\"uuid\":\"" + sortData.getTableUuid() + "\"," +
				"\"sortColumnName\":\"" + sortData.getSortColumnName() + "\"," +
				"\"sortColumnType\":\"" + sortData.getSortColumnType() + "\"," +
				"\"sortOrder\":\"" + sortData.getSortOrder() + "\"" +
				//",\"sortDatasetName\":\"TableReport_TableDataset_1327925074017_135700\"" +
				"}}"
				)
			);
		
		ReportContext reportContext = context.getExporter().getReportContext();
		parameters.addParameter(new JRPrintHyperlinkParameter(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID, String.class.getName(), reportContext.getId()));
		parameters.addParameter(new JRPrintHyperlinkParameter(ReportServlet.REQUEST_PARAMETER_RUN_REPORT, String.class.getName(), "true"));
		
		hyperlink.setHyperlinkParameters(parameters);
		
		return context.getHyperlinkURL(hyperlink);
	}
	
	private String getFilterFormActionLink(JRHtmlExporterContext context) {
		JRBasePrintHyperlink hyperlink = new JRBasePrintHyperlink();
		hyperlink.setLinkType(ReportExecutionHyperlinkProducer.HYPERLINK_TYPE_REPORT_EXECUTION);
		
		JRPrintHyperlinkParameters parameters = new JRPrintHyperlinkParameters();
		ReportContext reportContext = context.getExporter().getReportContext();
		parameters.addParameter(new JRPrintHyperlinkParameter(WebReportContext.REQUEST_PARAMETER_REPORT_CONTEXT_ID, String.class.getName(), reportContext.getId()));
		parameters.addParameter(new JRPrintHyperlinkParameter(ReportServlet.REQUEST_PARAMETER_RUN_REPORT, String.class.getName(), "true"));
		
		hyperlink.setHyperlinkParameters(parameters);
		
		return context.getHyperlinkURL(hyperlink);
	}

	private String getCurrentSortField(JasperReportsContext jasperReportsContext, ReportContext reportContext, String uuid, String sortDatasetName, String sortColumnName, String sortColumnType) 
	{
//		String currentSortDataset = (String) reportContext.getParameterValue(
//				SortElement.REQUEST_PARAMETER_DATASET_RUN);
//		if (sortDatasetName == null || !sortDatasetName.equals(currentSortDataset))
//		{
//			// sorting is on a different dataset
//			return null;
//		}
//		
//		String currentTableSortFieldsParam = currentSortDataset + SortElement.SORT_FIELDS_PARAM_SUFFIX;
//		@SuppressWarnings("unchecked")
//		List<JRSortField> existingFields = (List<JRSortField>) reportContext.getParameterValue(currentTableSortFieldsParam);

		JasperDesignCache cache = JasperDesignCache.getInstance(jasperReportsContext, reportContext);
		SortAction action = new SortAction();
		action.init(jasperReportsContext, reportContext);
		CommandTarget target = action.getCommandTarget(UUID.fromString(uuid));
		if (target != null)
		{
			JasperDesign jasperDesign = cache.getJasperDesign(target.getUri());
			JRDesignDataset dataset = (JRDesignDataset)jasperDesign.getMainDataset();
			
			List<JRSortField> existingFields =  dataset.getSortFieldsList();
			String sortField = null;
	
			if (existingFields != null && existingFields.size() > 0) {
				for (JRSortField field: existingFields) {
					if (field.getName().equals(sortColumnName) && field.getType().getName().equals(sortColumnType)) {
						sortField = sortColumnName + HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR + sortColumnType + HeaderToolbarElement.SORT_COLUMN_TOKEN_SEPARATOR;
						switch (field.getOrderValue()) {
							case ASCENDING:
								sortField += HeaderToolbarElement.SORT_ORDER_ASC;
								break;
							case DESCENDING:
								sortField += HeaderToolbarElement.SORT_ORDER_DESC;
								break;
						}
						break;
					}
				}
			}
		
			return sortField;
		}
		
		return null;
	}
	
	public boolean toExport(JRGenericPrintElement element) {
		return true;
	}
	
	private Map<String, String> getTranslatedOperators(
		JasperReportsContext jasperReportsContext, 
		String bundleName, 
		JREnum[] operators, 
		Locale locale
		) 
	{
		Map<String, String> result = new LinkedHashMap<String, String>();
		MessageProvider messageProvider = MessageUtil.getInstance(jasperReportsContext).getMessageProvider(bundleName);
		
		for (JREnum operator: operators) {
			result.put(((Enum<?>)operator).name(), messageProvider.getMessage(((Enum<?>)operator).name(), null, locale));
		}
		
		return result;
	}
	
	public static void getFieldFilters(DatasetFilter existingFilter, List<FieldFilter> fieldFilters, String fieldName) {//FIXMEJIVE put this in some util and reuse
		if (existingFilter instanceof FieldFilter) {
			if ( fieldName == null || (fieldName != null && ((FieldFilter)existingFilter).getField().equals(fieldName))) {
				fieldFilters.add((FieldFilter)existingFilter);
			} 
		} else if (existingFilter instanceof CompositeDatasetFilter) {
			for (DatasetFilter filter : ((CompositeDatasetFilter)existingFilter).getFilters())
			{
				getFieldFilters(filter, fieldFilters, fieldName);
			}
		}
	}
	
	private String getJsonString(List<FieldFilter> fieldFilters) {
		
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter(128);
		try {
			mapper.writeValue(writer, fieldFilters);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new JRRuntimeException(e);
		}
		
		return writer.getBuffer().toString();
	}
}