package com.ifast.common.tags.processor;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.templatemode.TemplateMode;

import com.ifast.common.service.DictService;
import com.ifast.common.tags.util.IftgUtil;
import com.ifast.common.tags.vo.ValueVO;

/**
 * select 注解 用法 <iftg:select dicType = "dic_of_sex"></iftg:select> 属性
 * dicType是必填项 --情况1：当dicType = 字典中的type 时select下拉数值渲染的value 为字典中的name字段，name
 * 为字典中的name字段
 *
 *
 * --情况2：当dicType = all时候表示select下拉数值渲染的value 为字典中的type字段，name
 * 为字典中的description字段）
 *
 * 注： 控件的其他属性，用户可根据需求完全自定义，如需要加上name属性和Id属性则
 * <iftg:select dicType = "dic_of_sex" name="mySelect" id=
 * "selectId"></iftg:select>
 * 
 * @author: zet
 * @date:2018/8/22
 */

public class IftgSelectProcessor extends AbstractElementTagProcessor {
	private DictService dictService;
	private boolean firstLoad = true;

	private static final String ATTR_NAME = "select";
	private static final int PRECEDENCE = 120;

	public IftgSelectProcessor(final String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, ATTR_NAME, true, null, false, PRECEDENCE);

		// 这段是从网上找的
		/*
		 * super(TemplateMode.HTML, // This processor will apply only to HTML mode
		 * dialectPrefix, // Prefix to be applied to name for matching null, // No tag
		 * name: match any tag name false, // No prefix to be applied to tag name
		 * ATTR_NAME, // Name of the attribute that will be matched true, // Apply
		 * dialect prefix to attribute name PRECEDENCE // Precedence (inside dialect's
		 * precedence) ); // Remove the matched attribute afterwards
		 */ }

	/**
	 * 核心处理
	 *
	 * @param arguments thymeleaf 上下文对象
	 * @param element   当前节点对象
	 * @return
	 */
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		// 初始化
		init(context);
		// 获取值
		String dicType = tag.getAttributeValue("dicType");// 字典类型
		String defaultValue = tag.getAttributeValue("defaultValue");// 默认选中

		String thValue = IftgUtil.getTargetAttributeValue(context, tag, "th:value");// 回显值
		String defaultSelect = StringUtils.isNoneBlank(thValue) ? thValue : defaultValue;
		List<ValueVO> valueVos = IftgUtil.getValues(dictService, dicType, new String[] { defaultSelect });
		// 创建对象
		createSelect(context, valueVos, structureHandler);
	}
	// protected List<Node> getMarkupSubstitutes(Arguments arguments, Element
	// element) {}

	/**
	 * 初始化
	 *
	 * @param argument
	 */
	private void init(ITemplateContext context) {
		if (firstLoad) {
			ApplicationContext appCtx = SpringContextUtils.getApplicationContext((ITemplateContext) context);
			dictService = appCtx.getBean(DictService.class);
			firstLoad = false;
		}
	}

	/**
	 * 创建select对象
	 *
	 * @param attributeMap select 属性值
	 * @param options      下拉值
	 * @return
	 * 
	 * 		创建将替换自定义标签的DOM结构。 name将显示在“<span>”标签内, 因此必须首先创建, 然后必须向其中添加一个节点。
	 * 
	 */
	private void createSelect(ITemplateContext context, List<ValueVO> options,
			IElementTagStructureHandler structureHandler) {
		final IModelFactory modelFactory = context.getModelFactory();

		final IModel model = modelFactory.createModel();
		model.add(modelFactory.createOpenElementTag(
				"select dicType='all' id= 'data-placeholder'  class='form-control chosen-select'  tabindex='2' style='width: 100%'"));
		model.add(modelFactory.createOpenElementTag("option"));
		model.add(modelFactory.createText("选择类别"));
		model.add(modelFactory.createCloseElementTag("option"));

		// 创建option
		for (ValueVO option : options) {

			model.add(modelFactory.createOpenElementTag(String.format("option value='%s'", option.getVlaue())));
			model.add(modelFactory.createText(option.getName()));
			model.add(modelFactory.createCloseElementTag("option"));
			IOpenElementTag el = modelFactory.createOpenElementTag("option");

//			optionEle = new Element("option");
//			optionEle.setAttribute("value", option.getVlaue());
//
//			// 默认选中
//			if (Objects.nonNull(option.getSelected()) && option.getSelected()) {
//				optionEle.setAttribute("selected", "selected");
//			}
//
//			optionEle.addChild(new Text(option.getName()));
//			selectEle.addChild(optionEle);
		}
		model.add(modelFactory.createCloseElementTag("select"));

		// 创建属性
//		if (Objects.nonNull(attributeMap)) {
//			for (String mapKey : attributeMap.keySet()) {
//				String key = mapKey;
//				String value = attributeMap.get(key).getValue();
//				selectEle.setAttribute(key, value);
//			}
//		}

		/*
		 * 指示引擎用指定的模型替换整个元素。
		 */
		// structureHandler.setBody(model, true);
		structureHandler.replaceWith(model, false);

		// return selectEle;
	}
}
