package ru.strid.strreward.entities;

import ru.strid.strreward.enums.RewardType;

public class Reward {
    private final String rewardTableName;
    private RewardType rewardType;
    private RewardContent rewardContent;
    private RewardContent usedRewardContent;
    private boolean depletable;
    private int numberOfSlots = 3;

    public Reward(String rewardTableName) {
        this.rewardTableName = rewardTableName;
        this.rewardType = RewardType.FULL_TABLE;
        this.rewardContent = new RewardContent();
        this.usedRewardContent = new RewardContent();
        this.depletable = false;
    }

    public RewardType getRewardType() {
        return rewardType;
    }

    public void setRewardType(RewardType rewardType) {
        this.rewardType = rewardType;
    }

    public RewardContent getRewardContent() {
        return rewardContent;
    }

    public void setRewardContent(RewardContent rewardContent) {
        this.rewardContent = rewardContent;
    }

    public RewardContent getUsedRewardContent() {
        return usedRewardContent;
    }

    public void setUsedRewardContent(RewardContent usedRewardContent) {
        this.usedRewardContent = usedRewardContent;
    }

    public boolean isDepletable() {
        return depletable;
    }

    public void setDepletable(boolean depletable) {
        this.depletable = depletable;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }
}
