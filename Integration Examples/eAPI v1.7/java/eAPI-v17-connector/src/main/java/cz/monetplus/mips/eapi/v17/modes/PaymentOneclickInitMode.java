package cz.monetplus.mips.eapi.v17.modes;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cz.monetplus.mips.eapi.v17.ArgsConfig;
import cz.monetplus.mips.eapi.v17.RunMode;
import cz.monetplus.mips.eapi.v17.ArgsConfig.RunModeEnum;
import cz.monetplus.mips.eapi.v17.connector.entity.PayRes;
import cz.monetplus.mips.eapi.v17.service.MipsException;
import cz.monetplus.mips.eapi.v17.service.NativeApiV17Service;
import cz.monetplus.mips.eapi.v17.service.RespCode;

@Component
public class PaymentOneclickInitMode implements RunMode {

	private static final Logger LOG = LoggerFactory.getLogger(PaymentOneclickInitMode.class);
	
	@Autowired
	private NativeApiV17Service nativeApiV17Service;
	
	@Override
	public void proc(ArgsConfig aConfig) {
		
		try {
			if (StringUtils.trimToNull(aConfig.oneclickFile) == null) {
				throw new MipsException(RespCode.INVALID_PARAM, "Missing mandatory parameter oneclickFile for paymentOneclickInit operation");
			}
			File f = new File(aConfig.oneclickFile);
			if (!f.isFile() || !f.canRead()) {
				throw new IllegalArgumentException("Unable to load recurrentFile " + f.getAbsolutePath());
			}
			
			PayRes res = nativeApiV17Service.paymentOneclickInit(f);
			LOG.info("result code: {} [{}], payment status {}, payId {}", res.resultCode, res.resultMessage, res.paymentStatus, res.payId);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

	@Override
	public RunModeEnum getMode() {
		return RunModeEnum.PAYMENT_ONECLICK_INIT;
	}

}
