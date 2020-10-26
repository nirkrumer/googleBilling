package getInvoicing;

import utils.ArgumentNames;
import com.google.ads.googleads.lib.GoogleAdsClient;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        long customerId = Long.parseLong(ArgumentNames.CUSTOMER_ID);
        GoogleAdsClient googleAdsClient = null;
        try {
            googleAdsClient = GoogleAdsClient.newBuilder().fromPropertiesFile().build();
        } catch (FileNotFoundException fnfe) {
            System.err.printf(
                    "Failed to load GoogleAdsClient configuration from file. Exception: %s%n", fnfe);
            System.exit(1);
        } catch (IOException ioe) {
            System.err.printf("Failed to create GoogleAdsClient. Exception: %s%n", ioe);
            System.exit(1);
        }

        SdkGetters sdkGetters = new SdkGetters();
        List<String> accountList = new ArrayList<>();
        accountList = sdkGetters.getCustomerClients(googleAdsClient, customerId);
//        sdkGetters.getCustomerClientsData(googleAdsClient, customerId);
//        sdkGetters.getCampaigns(googleAdsClient, customerId,accountList);
        sdkGetters.getCampaigns(googleAdsClient, customerId, Arrays.asList("2774065934"));
//        sdkGetters.getBudgetAccount(googleAdsClient, customerId, Arrays.asList("2774065934"));
    }
}