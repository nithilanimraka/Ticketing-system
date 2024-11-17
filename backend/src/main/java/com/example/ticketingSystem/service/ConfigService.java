package com.example.ticketingSystem.service;

import com.example.ticketingSystem.dto.configuration.AddConfigReqDTO;
import com.example.ticketingSystem.dto.configuration.AddConfigResponseDTO;
import com.example.ticketingSystem.entity.Configuration;
import com.example.ticketingSystem.entity.TicketPool;
import com.example.ticketingSystem.entity.Vendor;
import com.example.ticketingSystem.repository.ConfigurationRepository;
import com.example.ticketingSystem.repository.VendorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ConfigService {

    @Autowired
    private ConfigurationRepository configurationRepository;

//    private TicketManagementService ticketManagementService;
    @Autowired
    private TicketPoolService ticketPoolService;

//    @Autowired
//    public void setTicketManagementService(TicketManagementService ticketManagementService) {
//        this.ticketManagementService = ticketManagementService;
//    }

//    @Autowired
//    public ConfigService(ConfigurationRepository configurationRepository, TicketManagementService ticketManagementService) {
//        this.configurationRepository = configurationRepository;
//        this.ticketManagementService = ticketManagementService;
//        this.ticketPoolService = ticketPoolService;
//    }

//    @Autowired
//    private ConfigurationRepository configurationRepository;
//
//    @Autowired
//    private VendorRepository vendorRepository;
//
//    @Autowired
//    private TicketPoolService ticketPoolService;

//    @Transactional
//    public AddConfigResponseDTO addConfigs(AddConfigReqDTO addConfigReqDTO) {
//        Configuration configuration = new Configuration();
//        AddConfigResponseDTO responseAddConfig = new AddConfigResponseDTO();
//        try {
//
//            configuration.setEventName(addConfigReqDTO.getEventName());
//            configuration.setLocation(addConfigReqDTO.getLocation());
//            configuration.setNo_of_tickets(addConfigReqDTO.getNo_of_tickets());
//            configuration.setTicket_release_rate(addConfigReqDTO.getTicket_release_rate());
//            configuration.setCustomer_retrieval_rate(addConfigReqDTO.getCustomer_retrieval_rate());
//            configuration.setMax_tickets(addConfigReqDTO.getMax_tickets());
//
//            List<Vendor> vendors = addConfigReqDTO.getVendors();
//            if (vendors == null) {
//                vendors = new ArrayList<>();
//            } else {
//                for (Vendor vendor : vendors) {
//                    vendor.setConfiguration(configuration);
//                }
//            }
//            configuration.setVendors(vendors);
//
//            TicketPool ticketPool = ticketPoolService.createTicketPool(addConfigReqDTO.getMax_tickets());
//            configuration.setTicketPool(ticketPool);
//
//            configurationRepository.save(configuration);
//            log.info("configurations were added to the system");
//            responseAddConfig.setMessage("Configurations were successfully added to the system.");
//            responseAddConfig.setStatus(true);
//            return responseAddConfig;
//        } catch (Exception e) {
//            log.error("Error in saving configurations to the system : {}", e.getMessage());
//            responseAddConfig.setMessage("Configurations were not successfully added to the system.\n" + e.getMessage());
//            responseAddConfig.setStatus(false);
//            return responseAddConfig;
//        }
//    }

    public AddConfigResponseDTO addConfigs(AddConfigReqDTO addConfigReqDTO) {
        Configuration configuration = new Configuration();
        AddConfigResponseDTO responseAddConfig = new AddConfigResponseDTO();
        try{
            configuration.setEventName(addConfigReqDTO.getEventName());
            configuration.setLocation(addConfigReqDTO.getLocation());
            configuration.setNo_of_tickets(addConfigReqDTO.getNo_of_tickets());
            configuration.setTicket_release_rate(addConfigReqDTO.getTicket_release_rate());
            configuration.setCustomer_retrieval_rate(addConfigReqDTO.getCustomer_retrieval_rate());
            configuration.setMax_tickets(addConfigReqDTO.getMax_tickets());
            configuration.setCurrentTicketCount(0);
            configurationRepository.save(configuration);

            responseAddConfig.setMessage("Configurations were successfully added to the system.");
            responseAddConfig.setStatus(true);
            return responseAddConfig;

        } catch (Exception e){
            log.error("Error in saving configurations to the system : {}", e.getMessage());
            responseAddConfig.setMessage("Configurations were not successfully added to the system.\n" + e.getMessage());
            responseAddConfig.setStatus(false);
            return responseAddConfig;
        }
    }

//    public TicketPool getTicketPoolByConfigId(Long configId) {
//        Configuration configuration = configurationRepository.findById(configId)
//                .orElseThrow(() -> new RuntimeException("Configuration not found"));
//        return configuration.getTicketPool();
//    }
}