import DifficultyLevel0 from "./DifficultyLevel0";
import DifficultyLevel1 from "./DifficultyLevel1";
import DifficultyLevel2 from "./DifficultyLevel2";
import DifficultyLevel3 from "./DifficultyLevel3";
import DifficultyLevel4 from "./DifficultyLevel4";

export default ({ level }) => {
    switch (level) {
        case 0:
            return DifficultyLevel0();
        case 1:
            return DifficultyLevel1();
        case 2:
            return DifficultyLevel2();
        case 3:
            return DifficultyLevel3();
        case 4:
            return DifficultyLevel4();
    }
}