package lab5.commands;

import lab5.exceptions.ResourceException;
import lab5.model.Resource;
import lab5.repository.ResourceRepository;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

public class ReportCommand implements Command {
    private ResourceRepository repository;
    private String outputFile;

    public ReportCommand(ResourceRepository repository, String outputFile) {
        this.repository = repository;
        this.outputFile = outputFile;
    }

    @Override
    public void execute() throws ResourceException {
        try {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html>\n");
            html.append("<head>\n");
            html.append("    <title>Resource Catalog Report</title>\n");
            html.append("    <style>\n");
            html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
            html.append("        table { border-collapse: collapse; width: 100%; }\n");
            html.append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            html.append("        th { background-color: #4CAF50; color: white; }\n");
            html.append("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <h1>Resource Catalog Report</h1>\n");
            html.append("    <table>\n");
            html.append("        <tr>\n");
            html.append("            <th>ID</th>\n");
            html.append("            <th>Type</th>\n");
            html.append("            <th>Title</th>\n");
            html.append("            <th>Location</th>\n");
            html.append("            <th>Author</th>\n");
            html.append("            <th>Year</th>\n");
            html.append("        </tr>\n");

            Collection<Resource> resources = repository.getAllResources();
            for (Resource r : resources) {
                html.append("        <tr>\n");
                html.append("            <td>").append(escapeHtml(r.getId())).append("</td>\n");
                html.append("            <td>").append(escapeHtml(r.getType())).append("</td>\n");
                html.append("            <td>").append(escapeHtml(r.getTitle())).append("</td>\n");
                html.append("            <td>").append(escapeHtml(r.getLocation())).append("</td>\n");
                html.append("            <td>").append(escapeHtml(r.getAuthor() != null ? r.getAuthor() : "N/A")).append("</td>\n");
                html.append("            <td>").append(escapeHtml(r.getYear() != null ? r.getYear() : "N/A")).append("</td>\n");
                html.append("        </tr>\n");
            }

            html.append("    </table>\n");
            html.append("</body>\n");
            html.append("</html>\n");

            FileWriter writer = new FileWriter(outputFile);
            writer.write(html.toString());
            writer.close();

            System.out.println("Report created: " + outputFile);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(outputFile));
            }
        } catch (Exception e) {
            throw new ResourceException("Report failed: " + e.getMessage());
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
