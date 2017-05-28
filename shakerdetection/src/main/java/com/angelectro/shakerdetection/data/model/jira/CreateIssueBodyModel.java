package com.angelectro.shakerdetection.data.model.jira;

import com.google.gson.annotations.SerializedName;


public class CreateIssueBodyModel {

    @SerializedName("fields")
    private Field fields;

    public CreateIssueBodyModel(String title, String description, String projectId) {
        fields = new Field();
        fields.setProject(new Project());
        fields.setIssueType(new IssueType());
        fields.setDescription(description);
        fields.getProject().setId(projectId);
        fields.setTitle(title);
        fields.getIssueType().setName("Bug");
    }

    public Field getFields() {
        return fields;
    }

    public void setFields(Field fields) {
        this.fields = fields;
    }


    private class Field {
        @SerializedName("project")
        private Project project;
        @SerializedName("summary")
        private String title;
        @SerializedName("description")
        private String description;

        public IssueType getIssueType() {
            return issueType;
        }

        public void setIssueType(IssueType issueType) {
            this.issueType = issueType;
        }

        @SerializedName("issuetype")
        private IssueType issueType;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Project getProject() {
            return project;
        }

        public void setProject(Project project) {
            this.project = project;
        }
    }

    private class Project {
        @SerializedName("id")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private class IssueType{
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
