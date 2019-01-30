package com.ifast.common.tags.processor;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
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

public class KencooSelectProcessor extends AbstractElementTagProcessor {
	private static final String ELEMENT_NAME = "select";
	private static final int PRECEDENCE = 300;

	public KencooSelectProcessor(String dialectPrefix) {
		//super(TemplateMode.HTML, dialectPrefix, ELEMENT_NAME, true, ELEMENT_NAME, false, PRECEDENCE);
		 super(TemplateMode.HTML, // This processor will apply only to HTML mode
				dialectPrefix, // Prefix to be applied to name for matching
				ELEMENT_NAME, // Tag name: match specifically this tag
				true, // Apply dialect prefix to tag name
				null, // No attribute name: will match by tag name
				false, // No prefix to be applied to attribute name
				PRECEDENCE); // Precedence (inside dialect's own precedence)
 	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler handler) {
		System.out.println("kencoo:select");

		// 获取值
		String dicType = tag.getAttributeValue("dicType");// 字典类型
		String defaultValue = tag.getAttributeValue("defaultValue");// 默认选中

		ApplicationContext ctx = SpringContextUtils.getApplicationContext((ITemplateContext) context);
		DictService dictService = ((BeanFactory) ctx).getBean(DictService.class);

		String thValue = IftgUtil.getTargetAttributeValue(context, tag, "th:value");// 回显值
		String defaultSelect = StringUtils.isNoneBlank(thValue) ? thValue : defaultValue;
		List<ValueVO> valueVos = IftgUtil.getValues(dictService, dicType, new String[] { defaultSelect });
		// 创建对象
		createSelect(context, valueVos, handler);

	}

	private void createSelect(ITemplateContext context, List<ValueVO> options,
			IElementTagStructureHandler structureHandler) {
		final IModelFactory modelFactory = context.getModelFactory();

		final IModel model = modelFactory.createModel();
		model.add(modelFactory.createOpenElementTag("select"));
		model.add(modelFactory.createOpenElementTag("option"));
		model.add(modelFactory.createText("选择类别"));
		model.add(modelFactory.createCloseElementTag("option"));

		// 创建option
		for (ValueVO option : options) {

			model.add(modelFactory.createOpenElementTag(String.format("option value='%s'", option.getVlaue())));
			model.add(modelFactory.createText(option.getName()));
			model.add(modelFactory.createCloseElementTag("option"));
			IOpenElementTag el = modelFactory.createOpenElementTag("option");

		}
		model.add(modelFactory.createCloseElementTag("select"));

		structureHandler.replaceWith(model, false);

	}
}
