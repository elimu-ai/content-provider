package ai.elimu.content_provider.room.entity;

import androidx.annotation.NonNull;

/**
 * For documentation, see <a href="https://github.com/elimu-ai/webapp/tree/main/src/main/java/ai/elimu/model">model</a>
 */
public class Content extends BaseEntity {

    @NonNull
    private Integer revisionNumber;

    private Integer usageCount;

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
}
