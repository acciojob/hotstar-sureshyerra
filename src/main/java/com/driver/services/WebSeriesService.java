package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        ProductionHouse productionHouse;
        productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();



      if(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()).getSeriesName() != null){
           throw new Exception("Series is already present");
      }
        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        productionHouse.getWebSeriesList().add(webSeries);
        webSeries.setProductionHouse(productionHouse);
        int size = productionHouse.getWebSeriesList().size();
        double sum = 0;
        for(WebSeries webSeries1 : productionHouse.getWebSeriesList()){
            sum += webSeries1.getRating();
        }
        double newRatings = (double) sum/size;
        productionHouse.setRatings(newRatings);
        productionHouseRepository.save(productionHouse);
        WebSeries webSeries1 = webSeriesRepository.save(webSeries);

      return  webSeries1.getId();




        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

    }

}
