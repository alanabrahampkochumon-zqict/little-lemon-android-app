import supabase from "../supabase.js";
// NOTE: Create Bucket in supabase for your project
/*
 * Uploads a file to a supabase bucket
 * @param bucketName: Name of the supabase bucket/folder to upload to
 * @param fileName: Filename that is saved in supabase
 * @param file: The file to be uploaded
 * @param upsert: Whether to update the existing file
 */
export async function uploadImage(bucketName, fileName, file, upsert) {
    const { data, error } = await supabase.storage
        .from(bucketName)
        .upload(fileName, file, { upsert: upsert });

    if (error) console.error(`Error uploading image: ${error.message}`);
    else return data;
}
