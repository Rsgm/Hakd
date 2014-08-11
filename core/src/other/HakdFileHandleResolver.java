package other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class HakdFileHandleResolver implements FileHandleResolver {

    @Override
    public FileHandle resolve(String fileName) {
        if (!Util.ASSETS.isEmpty() && !fileName.startsWith(Util.ASSETS)) {
            fileName = Util.ASSETS + fileName;
        }

        return Gdx.files.internal(fileName);
    }
}
