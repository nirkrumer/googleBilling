package CostDTO;

import java.util.Currency;

import com.google.protobuf.StringValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor

public class GoogleAccountDTO {
    private String accountId;
    private StringValue accountName;
    private StringValue currencyCode;

    @Override
    public String toString() {
        return "GoogleAccountDTO{" +
                "accountId='" + accountId + '\'' +
                ", accountName='" + accountName + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }

    public GoogleAccountDTO(String accountId, StringValue accountName, StringValue currencyCode) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.currencyCode = currencyCode;
    }
}
