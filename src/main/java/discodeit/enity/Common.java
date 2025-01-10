package discodeit.enity;

import java.util.UUID;

public abstract class Common {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public Common(){
        this.id=UUID.randomUUID();
        this.createdAt=System.currentTimeMillis();
        this.updatedAt=createdAt;
    }


    //Getter
    public UUID getId(){
        return id;
    }
    public Long getCreatedAt(){
        return createdAt;
    }
    public Long getUpdatedAt(){
        return updatedAt;
    }

    //Update
    public void updateClass(){
        this.updatedAt=System.currentTimeMillis();
    }
}
