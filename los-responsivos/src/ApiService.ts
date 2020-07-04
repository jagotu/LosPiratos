import Team from "./models/Team";
import mockData from "./losTestos.json";

export default class ApiService {
    static getTeamData(): Promise<Team>{
        return new Promise<Team>((resolve) => resolve(mockData.teams[0] as Team));
    }
}