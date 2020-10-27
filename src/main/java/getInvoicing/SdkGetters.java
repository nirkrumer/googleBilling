package getInvoicing;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v2.errors.GoogleAdsException;
import com.google.ads.googleads.v5.services.*;;
import com.google.ads.googleads.v5.services.GoogleAdsServiceClient.SearchPagedResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import CostDTO.GoogleAccountDTO;

public class SdkGetters {
    static int PAGE_SIZE = 50 ;
    public List<String> getCustomerClients(GoogleAdsClient googleAdsClient, long customerId) {
        String query = "SELECT customer_client.client_customer, customer_client.resource_name FROM customer_client";
        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            SearchGoogleAdsRequest request =
                    SearchGoogleAdsRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(query)
                            .build();

            SearchPagedResponse response = googleAdsServiceClient.search(request);
            return StreamSupport.stream(response.iterateAll().spliterator(), false)
                    .map(googleAdsRow -> {
                        String[] accountArray = googleAdsRow.getCustomerClient().getResourceName().split("/");
                        return accountArray[accountArray.length-1];
                        }
                    )
                    .collect(Collectors.toList()) ;
        } catch (GoogleAdsException gae) {
            System.err.printf(
                    "Request ID %s failed due to GoogleAdsException. Underlying errors:%n",
                    gae.getRequestId());
            return null;
        }
    }

    public List<GoogleAccountDTO> getCustomerClientsData(GoogleAdsClient googleAdsClient, long customerId) {
        String query = "SELECT customer_client.resource_name,customer_client.descriptive_name, customer_client.currency_code FROM customer_client";
        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            SearchGoogleAdsRequest request =
                    SearchGoogleAdsRequest.newBuilder()
                            .setCustomerId(Long.toString(customerId))
                            .setQuery(query)
                            .build();

            SearchPagedResponse response = googleAdsServiceClient.search(request);
            return StreamSupport.stream(response.iterateAll().spliterator(), false)
                    .map(row -> new GoogleAccountDTO(
                                    row.getCustomerClient().getResourceName(),
                                    row.getCustomerClient().getDescriptiveName(),
                                    row.getCustomerClient().getCurrencyCode()
                            )
                    )
                    .collect(Collectors.toList());
        } catch (GoogleAdsException gae) {
            System.err.printf(
                    "Request ID %s failed due to GoogleAdsException. Underlying errors:%n",
                    gae.getRequestId());
            return null;
        }
    }

    public void getCampaigns(GoogleAdsClient googleAdsClient, long customerId, List<GoogleAccountDTO> accountList) {
        String query = "SELECT campaign.id, campaign.name, metrics.cost_micros FROM campaign " +
                "WHERE segments.date >= '2020-09-01' AND segments.date <= '2020-09-30' " +
                "AND metrics.cost_micros > 0 " +
                "ORDER BY campaign.name";

        try (GoogleAdsServiceClient googleAdsServiceClient =
                     googleAdsClient.getLatestVersion().createGoogleAdsServiceClient()) {
            System.out.println("account_id,account_name,campaign_id,campaign_name,currency,cost");
            accountList.stream()
//                    .limit(6)
                    .filter(googleAccountDTO->(googleAccountDTO.getAccountId().split("/")[3].equals("2774065934")
//                            || (googleAccountDTO.getAccountId().split("/")[3].equals("1119924918"))
                    ))
                    .forEach(googleAccountDTO-> {
                    String[] accountIdArray = googleAccountDTO.getAccountId().split("/");
                    SearchPagedResponse response = googleAdsServiceClient.search(
                        accountIdArray[accountIdArray.length-1], query);
                    StreamSupport.stream(response.iterateAll().spliterator(), false)
                        .forEach(row-> System.out.printf("%s,%s,%s,%s,%s,%s%n",
                                accountIdArray[accountIdArray.length-1],googleAccountDTO.getAccountName().getValue(),
                            row.getCampaign().getId(), row.getCampaign().getName(),
                                googleAccountDTO.getCurrencyCode().getValue(),row.getMetrics().getCostMicros()/(1000000.00)));
                })
            ;
        } catch (GoogleAdsException gae) {
            System.err.printf(
                    "Request ID %s failed due to GoogleAdsException. Underlying errors:%n",
                    gae.getRequestId());
        }
    }
}