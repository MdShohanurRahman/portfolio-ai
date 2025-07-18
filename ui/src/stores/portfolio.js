import { defineStore } from 'pinia'

export const usePortfolioStore = defineStore('portfolio', {
    state: () => ({
        experienceData: [
            {
                title: "Senior Java Developer",
                company: "Tech Solutions Inc.",
                duration: "Jan 2022 - Present",
                contributions: [
                    "Led the development of scalable microservices using Spring Boot and Kafka, improving system throughput by 30%.",
                    "Implemented robust RESTful APIs, ensuring high performance and security for critical applications.",
                    "Mentored junior developers and conducted code reviews, fostering a collaborative development environment."
                ]
            },
            // ... other experience data
        ],
        // ... other portfolio data (education, skills, projects, etc.)
        blogLink: "https://yourblog.hashnode.dev",
        resumeDownloadLink: "https://example.com/your_resume.pdf",
        linkedinProfile: "https://www.linkedin.com/in/yourprofile",
        facebookProfile: "https://www.facebook.com/yourprofile"
    }),
    getters: {
        // Add any computed properties if needed
    },
    actions: {
        // Add methods to update portfolio data if needed
    }
})