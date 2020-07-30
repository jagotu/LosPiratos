export type EnhancementStatus = "active" | "empty" | "destroyed"

export default interface Enhancements {
    CannonUpgrade?: EnhancementStatus;
    ChainShot?: EnhancementStatus;
    HeavyShot?: EnhancementStatus;
    HullUpgrade?: EnhancementStatus;
    Mortar?: EnhancementStatus;
    Ram?: EnhancementStatus;
};