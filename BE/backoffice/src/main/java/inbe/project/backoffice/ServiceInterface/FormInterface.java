package inbe.project.backoffice.ServiceInterface;

import inbe.project.backoffice.RequestDTO.DownloadFileDTO;
import inbe.project.backoffice.RequestDTO.FormQueryDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface FormInterface {


    void getForm();

    Object getResponse(FormQueryDTO formQueryDTO);

    String downloadFile(DownloadFileDTO downloadFileDTO) throws IOException;

    Resource downloadFileFromStorage(DownloadFileDTO downloadFileDTO) throws IOException;
}
