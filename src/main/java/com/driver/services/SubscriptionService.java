package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        if(subscriptionEntryDto.equals("ELITE")){
            subscription.setTotalAmountPaid(1000 + 350 * subscriptionEntryDto.getNoOfScreensRequired());
        } else if (subscriptionEntryDto.getSubscriptionType().equals("PRO")) {
            subscription.setTotalAmountPaid(800 + 250 * subscriptionEntryDto.getNoOfScreensRequired());
        } else if (subscriptionEntryDto.getSubscriptionType().equals("BASIC")) {
            subscription.setTotalAmountPaid(500 + 200 * subscriptionEntryDto.getNoOfScreensRequired());
        }
        user.setSubscription(subscription);
        subscription.setUser(user);

        userRepository.save(user);
        return subscription.getTotalAmountPaid();




    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        int amount = 0;
        User user = userRepository.findById(userId).get();
        if(user.getSubscription().getSubscriptionType().equals("ELITE")){
            throw new Exception("Already the best Subscription");
        }
        if (user.getSubscription().getSubscriptionType().equals("BASIC")) {

            int oldprice = user.getSubscription().getTotalAmountPaid();
            int newprice = 800 + 250  * user.getSubscription().getNoOfScreensSubscribed();
            user.getSubscription().setTotalAmountPaid(newprice);

            user.getSubscription().setSubscriptionType(SubscriptionType.PRO);
            amount = newprice-oldprice;
        } else if (user.getSubscription().getSubscriptionType().equals("PRO")) {
            int oldprice = user.getSubscription().getTotalAmountPaid();
            int newprice = 1000 + 350 * user.getSubscription().getNoOfScreensSubscribed();
            user.getSubscription().setTotalAmountPaid(newprice);
            user.getSubscription().setSubscriptionType(SubscriptionType.ELITE);
            amount = newprice-oldprice;
        }
        userRepository.save(user);
        return amount;

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int total = 0;
        for (Subscription subscription : subscriptionRepository.findAll()){
            total+= subscription.getTotalAmountPaid();
        }
        return total;
    }

}
