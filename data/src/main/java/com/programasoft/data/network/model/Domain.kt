package com.programasoft.data.network.model

enum class Domain(val id: Int, val displayName: String, val description: String) {
    CLINICAL_PSYCHOLOGY(
        1,
        "Clinical psychology",
        "Specializes in the study and treatment of mental and emotional disorders"
    ),
    WORK_PSYCHOLOGY(
        2,
        "Work psychology",
        "Specializes in the study of interactions between individuals and their work environment"
    ),
    RELAXATION_PSYCHOLOGY(
        3,
        "Relaxation psychology",
        "Specializes in the study of relaxation and meditation techniques"
    ),
    ADDICTION_STUDIES(
        4,
        "Addiction studies",
        "Specializes in the study and treatment of behavioral and/or substance addictions"
    ),
    CHILD_PSYCHOLOGY(
        5,
        "Child psychology",
        "Specializes in the study and treatment of developmental disorders in children"
    ),
    ADOLESCENT_PSYCHOLOGY(
        6,
        "Adolescent psychology",
        "Specializes in the study and treatment of psychological disorders in adolescents"
    ),
    NEUROPSYCHOLOGY(
        7,
        "Neuropsychology",
        "Specializes in the study of the relationship between the brain and human behavior"
    );

    companion object {
        fun fromId(id: Int): Domain? = values().find { it.id == id }
    }
}