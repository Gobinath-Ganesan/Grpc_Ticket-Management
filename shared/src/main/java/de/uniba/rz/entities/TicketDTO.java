package de.uniba.rz.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "TicketDTO")

public class TicketDTO {
        private int id;
        private String reporter;
        private String topic;
        private String description;
        private Type type;
        private Priority priority;
        private Status status;

        public TicketDTO(final Ticket ticket){
            this.id = ticket.getId();
            this.reporter=ticket.getReporter();
            this.description=ticket.getDescription();
            this.topic=ticket.getTopic();
            this.type=ticket.getType();
            this.priority=ticket.getPriority();
            this.status=ticket.getStatus();
        }

        public TicketDTO(){

        }

        public Ticket convertTicketDTOToTicket(){
            return new Ticket(this.getId(),this.getReporter(),this.getTopic(),this.getDescription(),this.getType(),this.getPriority(),this.getStatus());
        }



        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getReporter() {
            return reporter;
        }

        public void setReporter(String reporter) {
            this.reporter = reporter;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Priority getPriority() {
            return priority;
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }


}
