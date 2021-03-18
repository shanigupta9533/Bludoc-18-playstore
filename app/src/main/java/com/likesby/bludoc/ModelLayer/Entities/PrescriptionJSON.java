package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;

public class PrescriptionJSON{

	@SerializedName("doctor_id")
	private String doctorId;

	@SerializedName("medicines")
	private ArrayList<MedicinesItem> medicines;

	public ArrayList<LabTestItem> getLabTest() {
		return labTest;
	}

	public void setLabTest(ArrayList<LabTestItem> labTest) {
		this.labTest = labTest;
	}

	public String getEndNote() {
		return endNote;
	}

	public void setEndNote(String endNote) {
		this.endNote = endNote;
	}

	@SerializedName("lab_test")
	private ArrayList<LabTestItem> labTest;

	@SerializedName("end_note")
	private String endNote;

	@SerializedName("patient_id")
	private String patientId;

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	@SerializedName("diagnosis")
	private String diagnosis;

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public void setMedicines(ArrayList<MedicinesItem> medicines){
		this.medicines = medicines;
	}

	public ArrayList<MedicinesItem> getMedicines(){
		return medicines;
	}

	public void setPatientId(String patientId){
		this.patientId = patientId;
	}

	public String getPatientId(){
		return patientId;
	}

	@Override
 	public String toString(){
		return 
			"PrescriptionJSON{" + 
			"doctor_id = '" + doctorId + '\'' + 
			",medicines = '" + medicines + '\'' + 
			",patient_id = '" + patientId + '\'' + 
			",end_note = '" + endNote + '\'' +
			",lab_test = '" + labTest + '\'' +
			"}";
		}
}