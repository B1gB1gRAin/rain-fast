package com.ifast.common.tags;

import com.ifast.common.tags.processor.IftgSelectProcessor;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import java.util.HashSet;
import java.util.Set;


/**
 * 自定注解处理入口
 *
 * @author: zet
 * @date:2018/8/22
 */
@Component
public class IftgDialectEntrance extends AbstractProcessorDialect {
	private static final String DIALECT_NAME = "IftgDialect";

	private static final String PREFIX = "iftg";

	public IftgDialectEntrance() {
		super(DIALECT_NAME, PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		// <iftg:select/> 注解
		processors.add(new IftgSelectProcessor(dialectPrefix));
		// This will remove the xmlns:score attributes we might add for IDE validation
		//processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));

		return processors;
	}
}
