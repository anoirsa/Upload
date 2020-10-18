package uploadservlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.Part;

@WebServlet(name = "UploadSearch")
public class UploadSearch extends HttpServlet {
    private static final long serialVersionUID = 205242440643911308L;
    // The Hash
    //HashMap<String,String> usersMessage = new HashMap<String,String>();
    List<UserData> Users = new ArrayList<UserData>();

    String uploadFilePath;
    String relativePath;
    StringBuilder feedback = new StringBuilder("<p> All the files uploaded by users");

    public void init() {
        relativePath = getServletContext().getInitParameter("upload_path");
        uploadFilePath = this.getServletContext().getRealPath(relativePath) + File.separator;
        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
    }

    public void addFiles(String username,String comment,HttpServletRequest request,PrintWriter out) throws IOException, ServletException {
        String fileName = null;
        File fileObj = null;
        StringBuilder feedbackEach = new StringBuilder();
        //Collection<Part> parts = request.getParts();

        for(Part part : request.getParts()) {
            //fileName = part.getSubmittedFileName();
            fileName = getFileName(part);
            if(!fileName.equals("")){
                fileObj = new File(fileName);
                fileName = fileObj.getName();
                fileName = username +" "+fileName;
                fileObj = new File(uploadFilePath + fileName);
                part.write(fileObj.getAbsolutePath());
                // This is for all users
                feedback.append("<li>" + fileObj.getAbsolutePath() + " " + fileObj.length() + "<br><img src='"
                        + relativePath + File.separator + fileName
                        + "' width='200' height='200'>" +"<p>Uploaded by "+username+"</p><p>Comment : "+comment  +"</p></li>");
                // This is for the purpose when we filter for users
                feedbackEach.append("<li>" + fileObj.getAbsolutePath() + " " + fileObj.length() + "<br><img src='"
                        + relativePath + File.separator + fileName
                        + "' width='200' height='200'>" + "<p>Comment : "+comment  +"</p></li>");
           }
       }
        feedback.append("</ul>");
        feedbackEach.append("</ul>");
        UserData newUserData = new UserData(username,comment,feedbackEach.toString());
        Users.add(newUserData);
        /**
        out.println("<html><head><title>" + "Response of " + this.getServletConfig().getInitParameter("urlPatterns")
                + "</title></head><body><h1>Summary</h1>"); **/
        out.println(feedback.toString());



         }

         public void filterByName(String username,PrintWriter out ) {

             List<UserData> filtered = Users.stream().filter(user -> user.getUserName().equals(username)).collect(Collectors.toList());
             out.println("<p> Images uploaded by "+username+ " are : </p>");
             for (UserData data : filtered ) {
                 out.println(data.getFilePath());
             }
         }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("username");
        String comment = request.getParameter("comment");
        String action = request.getParameter("action");
        String filteredUser = request.getParameter("searchedUsername");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>" + "Response of " + this.getServletConfig().getInitParameter("urlPatterns")
                + "</title></head><body><h1>Summary</h1>");
        if(action.equals("Upload")) {addFiles(userName,comment,request,out);}
        else if (action.equals("Search"))  {filterByName(filteredUser,out); }

        out.println("<p style='text-align: center;'><a href='index.html'>Main Page</a></p>");
        out.println("</body></html>");
        out.close();




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            /*
             * In the following line we split a piece of text like the following: form-data;
             * name="fileName"; filename="C:\Users\Public\Pictures\Sample Pictures\img.jpg"
             */
            String[] tokens = contentDisp.split(";");
            for (String token : tokens) {
                if (token.trim().startsWith("filename")) {
                    return new File(token.split("=")[1].replace('\\', '/')).getName().replace("\"", "");
                }
            }
        }
        return "";
    }


    }