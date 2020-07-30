import ResourcesModel from "./Resources";

export default interface CostResponse {
    cost: ResourcesModel;
    isSatisfied: boolean;
}