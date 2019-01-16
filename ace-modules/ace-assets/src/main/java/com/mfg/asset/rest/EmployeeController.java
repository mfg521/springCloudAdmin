package com.mfg.asset.rest;

import com.mfg.asset.Vo.EmployeeBlocksVo;
import com.mfg.asset.Vo.EmployeeVo;
import com.mfg.asset.biz.EmployeeBiz;
import com.mfg.asset.commom.ApiResult;
import com.mfg.asset.entity.Employee;
import com.mfg.asset.entity.Room;
import com.mfg.asset.utils.FileUtil;
import com.mfg.auth.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("employee")
public class EmployeeController extends BaseController<EmployeeBiz, Employee> {

    @Autowired
    private EmployeeBiz employeeBiz;


    @RequestMapping(value = "/employeeUrl", method = RequestMethod.GET)
    public byte[] search_empPic(@RequestParam String employeeUrl) throws IOException {

        //assetQrcodeAddress=assetQrcodeAddress.replace("=","");
        String url = "/usr/vueProject/employeePic/" + employeeUrl;
//        String url = "/Users/mengfanguang/Desktop/pictrue/" + employeeUrl;
        System.out.println(url);

        File file = new File(url);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }

    @RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
    public String uploadImg(@RequestParam("file") MultipartFile file,
                            HttpServletRequest request) {

        String contentType = file.getContentType();   //图片文件类型
        String fileName = file.getOriginalFilename();  //图片名字

        //文件存放路径
        String filePath = "/usr/vueProject/employeePic/";
//        String filePath = "/Users/mengfanguang/Desktop/pictrue/";

        //调用文件处理类FileUtil，处理文件，将文件写入指定位置
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // 返回图片的存放路径
        return fileName;
    }


    @RequestMapping(value = "/qrCode", method = RequestMethod.GET)
    public byte[] search_qrcode(@RequestParam String assetQrcodeAddress) throws IOException {
        String path = FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "testQrcode/";
        String url = path + assetQrcodeAddress;
        File file = new File(url);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }


    @RequestMapping(value = "/changeEmpByDrop", method = RequestMethod.POST)
    public ApiResult<Integer> changeEmp(@RequestBody Employee employee) {
        return ApiResult.success(employeeBiz.changeEmpByDrop(employee));
    }

    @RequestMapping(value = "/changeOrSaveEmpBlock", method = RequestMethod.POST)
    public ApiResult<Integer> changeOrSaveEmpBlock(@RequestBody EmployeeVo employeeVo) {
        return ApiResult.success(employeeBiz.changeOrSaveEmpBlock(employeeVo));
    }

    @RequestMapping(value = "/getEmpBlockByRoomNum", method = RequestMethod.POST)
    public EmployeeBlocksVo getEmpBlockByRoomNum(@RequestBody Room room) {
        return employeeBiz.findByRooms(room);
    }
}
