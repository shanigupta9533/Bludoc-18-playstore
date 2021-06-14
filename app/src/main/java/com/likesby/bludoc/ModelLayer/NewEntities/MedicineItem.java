package com.likesby.bludoc.ModelLayer.NewEntities;

import com.google.gson.annotations.SerializedName;

public class MedicineItem {

    @SerializedName("m_e_d_i_c_i_n_e_i_d")
    private String presbMedicineId;

    @SerializedName("r_o_u_t_e")
    private String route;

    @SerializedName("p_r_e_s_b_p_a_t_i_e_n_t_i_d")
    private String presbPatientId;

    @SerializedName("i_n_s_t_r_u_c_t_i_o_n")
    private String instruction;

    @SerializedName("n_o_o_f_d_a_ys")
    private String noOfDays;

    @SerializedName("a_d_d_i_t_i_o_n_a_c_o_m_m_e_n_t")
    private String additionaComment;

    @SerializedName("q_t_y")
    private String qty;

    @SerializedName("m123_e_d_i_c123_i_n_e_n123_a_m_e")
    private String medicineName;

    @SerializedName("f_r_e_q_u_e_n_c_y")
    private String frequency;

    public String getPresbMedicineId() {
        return presbMedicineId;
    }

    public void setPresbMedicineId(String presbMedicineId) {
        this.presbMedicineId = presbMedicineId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPresbPatientId() {
        return presbPatientId;
    }

    public void setPresbPatientId(String presbPatientId) {
        this.presbPatientId = presbPatientId;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getAdditionaComment() {
        return additionaComment;
    }

    public void setAdditionaComment(String additionaComment) {
        this.additionaComment = additionaComment;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
