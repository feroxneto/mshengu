/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zm.hashcode.mshengu.repository.procurement.Impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import zm.hashcode.mshengu.app.util.DateTimeFormatHelper;
import zm.hashcode.mshengu.domain.fleet.Truck;
import zm.hashcode.mshengu.domain.procurement.Request;
import zm.hashcode.mshengu.domain.serviceprovider.ServiceProvider;
import zm.hashcode.mshengu.domain.ui.util.CostCentreType;
import zm.hashcode.mshengu.repository.procurement.RequestRepositoryCustom;

/**
 *
 * @author Colin
 */
public class RequestRepositoryImpl implements RequestRepositoryCustom {

    final DateTimeFormatHelper dateTimeFormatHelper = new DateTimeFormatHelper();
    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public List<Request> getTransactedRequestsByServiceProvider(ServiceProvider serviceProvider) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("serviceProviderSupplierId").is(serviceProvider.getId()).and("InvoiceNumber").ne(null));
        /*
         List<Request> transactedRequestList = mongoOperation.find(transactedRequestListQuery, Request.class);

         System.out.println("RequestRepository  - SERVICE PROVIDER WITH INVOICE NUMBER ONLY QUERY");
         if (transactedRequestList.isEmpty()) {
         System.out.println("RequestRepository  - SERVICE PROVIDER WITH INVOICE NUMBER ONLY QUERY - NO MATCHING RECORDS FOUND  FOR serviceProviderId: " + serviceProvider.getId());
         }

         for (Request request : transactedRequestList) {
         System.out.println("RequestRepository - SERVICE PROVIDER WITH INVOICE NUMBER ONLY QUERY -  serviceProviderId= " + request.getServiceProviderSupplierId() + " | truckId" + request.getTruckId() + " | DeliveryDATE= " + request.getDeliveryDate() + " | iNVoice= " + request.getInvoiceNumber());
         }
         System.out.println("--==--");
         */
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getTransactedRequestsBtnTwoDates(Date start, Date end) {
        Date startDate = dateTimeFormatHelper.resetTimeAndMonthStart(start);
        Date toDate = dateTimeFormatHelper.resetTimeAndMonthEnd(end);
        toDate = resetToMonthEndLastSecond(toDate);

        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("deliveryDate").exists(true)
                .andOperator(Criteria.where("deliveryDate").gte(startDate),
                Criteria.where("deliveryDate").lte(toDate)));

        /*
         List<Request> transactedRequestList = mongoOperation.find(transactedRequestListQuery, Request.class);

         System.out.println("RequestRepository  - GENERAL QUERY Start= " + start + " | To= " + end);
         if (transactedRequestList.isEmpty()) {
         System.out.println("RequestRepository  - GENERAL QUERY - NO MATCHING RECORDS FOUND");
         }

         for (Request request : transactedRequestList) {
         System.out.println("RequestRepository - GENERAL QUERY -  serviceProviderId= " + request.getServiceProviderSupplierId() + " | truckId" + request.getTruckId() + " | DeliveryDATE= " + request.getDeliveryDate() + " | iNVoice= " + request.getInvoiceNumber());
         }
         //        System.out.println("--==--");
         */
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getTransactedRequestsByTruckBtnTwoDates(Truck truck, Date start, Date end) {
        Date startDate = dateTimeFormatHelper.resetTimeAndMonthStart(start);
        Date toDate = dateTimeFormatHelper.resetTimeAndMonthEnd(end);
        toDate = resetToMonthEndLastSecond(toDate);
        return getTruckTransactedRequests(truck, startDate, toDate);
    }

    @Override
    public List<Request> getTransactedRequestsByTruckByMonth(Truck truck, Date month) {

        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);
        return getTruckTransactedRequests(truck, from, to);
    }

    @Override
    public List<Request> getTransactedRequestsByServiceProviderBtnTwoDates(ServiceProvider serviceProvider, Date start, Date end) {
        Date startDate = dateTimeFormatHelper.resetTimeAndMonthStart(start);
        Date toDate = dateTimeFormatHelper.resetTimeAndMonthEnd(end);
        toDate = resetToMonthEndLastSecond(toDate);
        return getServiceProviderTransactedRequest(serviceProvider, startDate, toDate);
    }

    @Override
    public List<Request> getTransactedRequestsByServiceProviderByMonth(ServiceProvider serviceProvider, Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);
        return getServiceProviderTransactedRequest(serviceProvider, from, to);
    }

    private List<Request> getTruckTransactedRequests(Truck truck, Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("truckId").is(truck.getId())
                .andOperator(Criteria.where("deliveryDate").gte(from),
                Criteria.where("deliveryDate").lte(to), Criteria.where("InvoiceNumber").ne(null)));

        /*
         List<Request> transactedRequestList = mongoOperation.find(transactedRequestListQuery, Request.class);

         System.out.println("RequestRepository  - TRUCK QUERY Start= " + from + " | To= " + to + "  FOR truckId: " + truck.getId());
         if (transactedRequestList.isEmpty()) {
         System.out.println("RequestRepository  - TRUCK QUERY - NO MATCHING RECORDS FOUND");
         }

         for (Request request : transactedRequestList) {
         System.out.println("RequestRepository - TRUCK QUERY -  serviceProviderId= " + request.getServiceProviderSupplierId() + " | truckId" + request.getTruckId() + " | DeliveryDATE= " + request.getDeliveryDate() + " | iNVoice= " + request.getInvoiceNumber());
         }
         //        System.out.println("--==--");
         */
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    private List<Request> getServiceProviderTransactedRequest(ServiceProvider serviceProvider, Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("serviceProviderSupplierId").is(serviceProvider.getId())
                .andOperator(Criteria.where("deliveryDate").gte(from),
                Criteria.where("deliveryDate").lte(to), Criteria.where("InvoiceNumber").ne(null)));

        /*
         List<Request> transactedRequestList = mongoOperation.find(transactedRequestListQuery, Request.class);

         System.out.println("RequestRepository  - SERVICE PROVIDER QUERY Start= " + from + " | To= " + to + "  FOR serviceProviderId: " + serviceProvider.getId());
         if (transactedRequestList.isEmpty()) {
         System.out.println("RequestRepository  - SERVICE PROVIDER QUERY - NO MATCHING RECORDS FOUND");
         }

         for (Request request : transactedRequestList) {
         System.out.println("RequestRepository - SERVICE PROVIDER QUERY -  serviceProviderId= " + request.getServiceProviderSupplierId() + " | truckId" + request.getTruckId() + " | DeliveryDATE= " + request.getDeliveryDate() + " | iNVoice= " + request.getInvoiceNumber());
         }
         //        System.out.println("--==--");
         */
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getProcessedRequestsWithInvoiceNumber() {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("InvoiceNumber").ne(null)
                .andOperator(Criteria.where("PaymentDate").exists(false)));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getServiceProviderProcessedRequestsWithInvoiceNumber(String serviceProviderId) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("serviceProviderSupplierId").is(serviceProviderId)
                .andOperator(Criteria.where("InvoiceNumber").ne(null),
                Criteria.where("PaymentDate").exists(false)));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getServiceProviderProcessedRequestsByMonth(ServiceProvider serviceProvider, Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);
        return getServiceProviderProcessedRequests(serviceProvider, from, to);
    }

    private List<Request> getServiceProviderProcessedRequests(ServiceProvider serviceProvider, Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("serviceProviderSupplierId").is(serviceProvider.getId())
                .andOperator(Criteria.where("PaymentDate").exists(true), Criteria.where("InvoiceNumber").ne(null)));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getProcessedRequestsWithPaymentDate(Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);
        return getAllServiceProviderProcessedRequestsWithPaymentDate(from, to);
    }

    private List<Request> getAllServiceProviderProcessedRequestsWithPaymentDate(Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("PaymentDate").exists(true)
                .andOperator(Criteria.where("DeliveryDate").gte(from),
                Criteria.where("DeliveryDate").lte(to)));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getServiceProviderProcessedRequestsWithPaymentDate(String serviceProviderId, Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);
        return getServiceProviderProcessedRequestsWithPaymentDate(serviceProviderId, from, to);
    }

    private List<Request> getServiceProviderProcessedRequestsWithPaymentDate(String serviceProviderId, Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("serviceProviderSupplierId").is(serviceProviderId)
                .andOperator(Criteria.where("PaymentDate").exists(true), Criteria.where("DeliveryDate").gte(from),
                Criteria.where("DeliveryDate").lte(to)));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getProcessedRequestsByCostCentreType(CostCentreType costCentreType, Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);
        return getServiceProviderProcessedRequestsByCostCentreType(costCentreType, from, to);
    }

    private List<Request> getServiceProviderProcessedRequestsByCostCentreType(CostCentreType costCentreType, Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("costCentreType").is(costCentreType)
                .andOperator(Criteria.where("InvoiceNumber").ne(null), Criteria.where("DeliveryDate").gte(from),
                Criteria.where("DeliveryDate").lte(to)));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getPendingRequests() {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("approver").exists(false));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getDisApprovedRequests() {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("reasonForDisapproval").exists(true));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getApprovedRequests(Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);
        return getProcessedApprovedRequests(from, to);
    }

    private List<Request> getProcessedApprovedRequests(Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("orderNumber").exists(true)
                .andOperator(Criteria.where("orderDate").ne(null), Criteria.where("orderDate").gte(from),
                Criteria.where("orderDate").lte(to), Criteria.where("orderDate").ne(null)));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> getApprovedRequestsBySupplier(String serviceProviderId, Date month) {
        Date from = dateTimeFormatHelper.resetTimeAndMonthStart(month);
        Date to = dateTimeFormatHelper.resetTimeAndMonthEnd(month);
        to = resetToMonthEndLastSecond(to);

        return getProcessedApprovedRequestsBySupplier(serviceProviderId, from, to);
    }

    private List<Request> getProcessedApprovedRequestsBySupplier(String serviceProviderId, Date from, Date to) {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("orderNumber").exists(true)
                .andOperator(Criteria.where("OrderDate").ne(null), Criteria.where("OrderDate").gte(from),
                Criteria.where("OrderDate").lte(to), Criteria.where("serviceProviderSupplierId").is(serviceProviderId)));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    @Override
    public List<Request> findByMisMatchStatus() {
        Query transactedRequestListQuery = new Query();
        transactedRequestListQuery.addCriteria(
                Criteria.where("matchStatus").gte("mismatch"));
        transactedRequestListQuery.with(new Sort(Sort.Direction.DESC, "orderNumber"));
        return mongoOperation.find(transactedRequestListQuery, Request.class);
    }

    private Date resetToMonthEndLastSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // Set time fields to last hour:minute:second:millisecond
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
