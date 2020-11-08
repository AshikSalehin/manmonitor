
package com.msbd.manmon.domainmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="last_relay_status_info")
public class LastRelayStatus {

    @Id
    @Column
    private String relayStatusId = "relayStatusId";
    
    @Column(name="last_relay_status")
    private String lastRelayStatus;

    public LastRelayStatus() {

    }

    public String getRelayStatusId() {
	return relayStatusId;
    }

//    public void setRelayStatusId(String relayStatusId) {
//	this.relayStatusId = relayStatusId;
//    }

    public String getLastRelayStatus() {
	return lastRelayStatus;
    }

    public void setLastRelayStatus(String lastRelayStatus) {
	this.lastRelayStatus = lastRelayStatus;
    }

}
