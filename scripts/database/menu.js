import path from "path";
import sharp from "sharp";
import { fileURLToPath } from "url";
import menuData from "../data/menu.json" with { type: "json" };
import supabase from "../supabase.js";

export async function insertNutritionInfo() {
    const { error, data } = await supabase
        .from("nutrition_info")
        .upsert(menuData.nutrition_info)
        .select("id");
    if (error) {
        console.error(
            `There was an error inserting nutrition info.\n${error.message}`,
        );
    } else {
        console.log(
            `Nutrition Info inserted successfully. ${data.length} records inserted!`,
        );
    }
}

export async function insertCategories() {
    const { error, data } = await supabase
        .from("category")
        .upsert(menuData.category)
        .select("id");

    if (error)
        console.error(
            `There was an error inserting nutrition info.\n${error.message}`,
        );
    else
        console.log(
            `Categories inserted successfully. ${data.length} records inserted!`,
        );
}

const IMAGE_PATH = "/../images/dishes/";

function getAbsolutePath(imageName) {
    const __filename = fileURLToPath(import.meta.url);
    const __dirname = path.dirname(__filename);
    return path.join(__dirname, IMAGE_PATH, imageName);
}

export async function insertDishes() {
    var exported = 0;
    for (let i = 0; i < menuData.dish.length; i++) {
        try {
            const imageName = menuData.dish[i].image;
            const fullPath = getAbsolutePath(imageName);
            console.log(fullPath);
            const metadata = await sharp(fullPath).metadata();
            exported++;
        } catch (e) {
            console.error(`Error reading image: ${e.message}`);
        }
    }
    console.log(exported, "files found!");
}
