package com.xzgedu.supercv.order.service;

import com.xzgedu.supercv.order.domain.PaymentChannel;
import com.xzgedu.supercv.order.repo.PaymentChannelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PaymentChannelService {

    @Autowired
    private PaymentChannelRepo paymentChannelRepo;

    public PaymentChannel getPaymentChannelById(long id) {
        return paymentChannelRepo.getPaymentChannelById(id);
    }

    public List<PaymentChannel> getAllPaymentChannels() {
        return paymentChannelRepo.getAllPaymentChannels();
    }

    public List<PaymentChannel> getEnabledPaymentChannelsByType(int paymentChannelType) {
        return paymentChannelRepo.getEnabledPaymentChannelsByType(paymentChannelType);
    }

    public PaymentChannel getEnabledPaymentChannelByType(int paymentChannelType) {
        List<PaymentChannel> paymentChannels = paymentChannelRepo.getEnabledPaymentChannelsByType(paymentChannelType);
        if (paymentChannels == null || paymentChannels.isEmpty()) return null;
        Random r = new Random();
        int size = paymentChannels.size();
        return paymentChannels.get(r.nextInt(size));
    }

    public boolean addPaymentChannel(PaymentChannel paymentChannel) {
        return paymentChannelRepo.addPaymentChannel(paymentChannel);
    }

    public boolean updatePaymentChannel(PaymentChannel paymentChannel) {
        return paymentChannelRepo.updatePaymentChannel(paymentChannel);
    }

    public boolean deletePaymentChannel(long id) {
        return paymentChannelRepo.deletePaymentChannel(id);
    }

    public List<Integer> getEnabledPaymentChannelTypes() {
        return paymentChannelRepo.getEnabledPaymentChannelTypes();
    }
}
