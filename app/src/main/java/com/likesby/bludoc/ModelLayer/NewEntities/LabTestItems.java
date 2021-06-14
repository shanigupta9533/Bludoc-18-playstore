package com.likesby.bludoc.ModelLayer.NewEntities;

import com.google.gson.annotations.SerializedName;

public class LabTestItems {

    @SerializedName("l_a_b_t_e_s_t_c_o_m_m_e_n_t")
    private String labTestComment;

    @SerializedName("l_a_b_t_e_s_t_n_a_m_e")
    private String labTestName;

    public String getLabTestComment() {
        return labTestComment;
    }

    public void setLabTestComment(String labTestComment) {
        this.labTestComment = labTestComment;
    }

    public String getLabTestName() {
        return labTestName;
    }

    public void setLabTestName(String labTestName) {
        this.labTestName = labTestName;
    }
}
