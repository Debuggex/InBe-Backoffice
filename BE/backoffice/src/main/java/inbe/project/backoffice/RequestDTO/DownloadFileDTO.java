package inbe.project.backoffice.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadFileDTO {
    private String fileURL;
    private String customerId;
}
