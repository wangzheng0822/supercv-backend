package com.xzgedu.supercv.order.repo;

import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.mapper.PaymentChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentChannelRepo {

    @Autowired
    private PaymentChannelMapper payChannelMapper;

    public PaymentChannel getPaymentChannelById(long id) {
        return payChannelMapper.selectPaymentChannelById(id);
    }

    public List<PaymentChannel> getAllPaymentChannels() {
        return payChannelMapper.selectAllPaymentChannels();
    }

    public List<PaymentChannel> getEnabledPaymentChannelsByType(int payChannelType) {
        return payChannelMapper.selectEnabledPaymentChannelsByType(payChannelType);
    }

    public boolean addPaymentChannel(PaymentChannel payChannel) {
        return payChannelMapper.insertPaymentChannel(payChannel) == 1;
    }

    public boolean updatePaymentChannel(PaymentChannel payChannel) {
        return payChannelMapper.updatePaymentChannel(payChannel) == 1;
    }

    public boolean deletePaymentChannel(long id) {
        return payChannelMapper.deletePaymentChannel(id) == 1;
    }

    public List<Integer> getEnabledPaymentChannelTypes() {
        return payChannelMapper.selectEnabledPaymentChannelTypes();
    }
}
