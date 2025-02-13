package com.example.ticketingSystem.service;

import com.example.ticketingSystem.dto.vendor.*;
import lombok.extern.slf4j.Slf4j;
import com.example.ticketingSystem.repository.VendorRepository;
import com.example.ticketingSystem.entity.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final TicketManagementService ticketManagementService;

    @Autowired
    public VendorService(TicketManagementService ticketManagementService) {
        this.ticketManagementService = ticketManagementService;
    }

    /**
     * Method to register a vendor
     * @param vendorRegisterReqDTO
     * @return
     */
    public VendorRegisterResponseDTO register(VendorRegisterReqDTO vendorRegisterReqDTO){
        Vendor vendor = new Vendor();
        VendorRegisterResponseDTO responseRegister = new VendorRegisterResponseDTO();
        try{
            vendor.setName(vendorRegisterReqDTO.getName());
            vendor.setEmail(vendorRegisterReqDTO.getEmail());
            vendor.setUsername(vendorRegisterReqDTO.getUsername());
            vendor.setPassword(this.passwordEncoder.encode(vendorRegisterReqDTO.getPassword()));
            vendor.setTickets_added(0);

            vendorRepository.save(vendor);
            log.info("Vendor saved in database");
            responseRegister.setMessage("Registration was successful with id : " + vendor.getVendor_id());
            return responseRegister;
        } catch (Exception e){
            log.error("Vendor saving unsuccessful : " + e.getMessage());
            responseRegister.setMessage("Registration was not successful");
            return responseRegister;
        }
    }

    /**
     * Method to login a registered vendor
     * @param vendorLoginReqDTO
     * @return
     */
    public VendorLoginResponseDTO login(VendorLoginReqDTO vendorLoginReqDTO){
        VendorLoginResponseDTO responseLogin = new VendorLoginResponseDTO();
        try{

            Vendor vendor = vendorRepository.findByUsername(vendorLoginReqDTO.getUsername());
            if(vendor != null){
                String password = vendorLoginReqDTO.getPassword();
                String encodedPassword = vendor.getPassword();
                boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
                if(isPwdRight){
                    Optional<Vendor> vendor1 = vendorRepository.findOneByUsernameAndPassword(vendorLoginReqDTO.getUsername(), encodedPassword);
                    if(vendor1.isPresent()){
                        responseLogin.setMessage("Login successful");
                        responseLogin.setUsername(vendorLoginReqDTO.getUsername());
                        responseLogin.setStatus(true);
                    }else {
                        responseLogin.setMessage("The credentials that have been entered are incorrect");
                        responseLogin.setStatus(false);
                    }
                }else {
                    responseLogin.setMessage("Password is incorrect");
                    responseLogin.setStatus(false);
                }

            }else {
                responseLogin.setMessage("Username does not exist");
                responseLogin.setStatus(false);
            }

            return responseLogin;
        } catch (Exception e){
            log.info("Login was unsuccessful : " + e.getMessage());
            responseLogin.setMessage("Error occurred while login");
            responseLogin.setStatus(false);
            return responseLogin;
        }
    }

    /**
     * Method to delete a vendor
     * @param vendorDeleteReqDTO
     * @return
     */
    public String deleteVendor(VendorDeleteReqDTO vendorDeleteReqDTO){
        Vendor vendor = vendorRepository.findById(vendorDeleteReqDTO.getId()).orElseThrow(null);
        vendorRepository.delete(vendor);
        log.info("Vendor with id " + vendorDeleteReqDTO.getId() + " was deleted successfully");
        return "Vendor was deleted successfully";
    }

    /**
     * Method for adding tickets to a configuration by a vendor
     * @param addTicketReqDTO
     * @return
     */
    public AddTicketResponseDTO addTickets(AddTicketReqDTO addTicketReqDTO){
        AddTicketResponseDTO addTicketResponseDTO = new AddTicketResponseDTO();
        int tickets_no = addTicketReqDTO.getCount();
        try{
            Boolean addTicketStatus = ticketManagementService.addTickets(addTicketReqDTO.getConfigId(), tickets_no);

            if(addTicketStatus){
                log.info("{} tickets added successfully", tickets_no);
                addTicketResponseDTO.setMessage(tickets_no + " tickets added successfully");
                addTicketResponseDTO.setStatus(true);
            } else {
                log.error("Error in adding tickets");
                addTicketResponseDTO.setMessage("Error in adding tickets. The number of tickets exceeds " +
                        "the total number of tickets available or the maximum ticket count in ticket pool.");
                addTicketResponseDTO.setStatus(false);
            }

        } catch (Exception e){
            log.error("Ticket addition was not successful : {}", e.getMessage());
            addTicketResponseDTO.setMessage("Error in adding tickets " + e.getMessage());
            addTicketResponseDTO.setStatus(false);
        }
        return addTicketResponseDTO;
    }

}
