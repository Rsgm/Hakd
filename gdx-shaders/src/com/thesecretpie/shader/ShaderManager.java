package com.thesecretpie.shader;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;

/**
 * @author Przemek Muller
 * This class simplifies usage of shaders and framebuffers. It can load shaders from files, fix them for GLES specifics and reload them at runtime.
 *
 */
public class ShaderManager {

	public static final String SHADER_CLASSPATH = "gdx-shaders/src/" + "com/thesecretpie/shader/shaders"; // added gdx-shaders/src/ to work with the project layout
	public static int MAX_FRAMEBUFFERS = 10;
	public static int FRAMEBUFFER_TEXTURE_ID = 0;

	private ObjectMap<String, ShaderProgram> shaders;
	private ObjectMap<String, String> shaderPaths;
	private ObjectMap<String, String> sourcesVert;
	private ObjectMap<String, String> sourcesFrag;
	private ObjectMap<String, FrameBuffer> frameBuffers;
	private Array<String> openedFrameBuffers;
	private String shaderDir;

	protected ShaderProgram currentShader = null;
	public String currentShaderIdn = null;
	public int currentTextureId = 0;
	private Mesh screenQuad;
	private OrthographicCamera screenCamera;

	private AssetManager am;

	/**
	 * Created new ShaderManager.
	 * @param shaderDir - path to the shader dir, set to "" if you just want to use built-in shaders
	 * @param am - your app's AssetManager instance
	 */

	public ShaderManager(String shaderDir, AssetManager am) {
		this(shaderDir, am, true);
	}

	public ShaderManager(String shaderDir, AssetManager am, boolean addProcessors) {
		shaders = new ObjectMap<String, ShaderProgram>();
		shaderPaths = new ObjectMap<String, String>();
		sourcesVert = new ObjectMap<String, String>();
		sourcesFrag = new ObjectMap<String, String>();
		frameBuffers = new ObjectMap<String, FrameBuffer>();
		openedFrameBuffers = new Array<String>(true, MAX_FRAMEBUFFERS);

		screenCamera = new OrthographicCamera(Gdx.graphics.getWidth()+2, Gdx.graphics.getHeight()+2);
		createScreenQuad();
		screenCamera.translate(0, -1);
		screenCamera.update();

		setShaderDir(shaderDir);
		setAssetManager(am);
		add("empty", "empty.vert", "empty.frag");
		add("default", "default.vert", "default.frag");
		if (addProcessors && (Gdx.app.getType() == ApplicationType.Desktop
				|| Gdx.app.getType() == ApplicationType.Applet
				|| Gdx.app.getType() == ApplicationType.WebGL)) {
			add("processor", "processor.vert", "processor.frag");
			add("processor_blur", "processor.vert", "processor_blur.frag");
			add("copy", "processor.vert", "copy.frag");
		}
	}

	/**
	 * Path to the shader dir, set to "" if you just want to use built-in shaders
	 * @param shaderDir
	 */
	public void setShaderDir(String shaderDir) {
		this.shaderDir = shaderDir;
	}

	/**
	 * Creates a new Framebuffer with given params.
	 * @param fbIdn - this framebuffer's identifier
	 * @param format - pixel format of this framebuffer
	 * @param fbWidth - desired width
	 * @param fbHeight - desired height
	 * @param hasDepth - whether to attach depth buffer
	 */
	public void createFB(String fbIdn, Format format, int fbWidth, int fbHeight, boolean hasDepth) {
		FrameBuffer fb = frameBuffers.get(fbIdn);
		if (fb == null || fb.getWidth() != fbWidth || fb.getHeight() != fbHeight) {
			if (fb != null)
				fb.dispose();
		    fb = new FrameBuffer(Format.RGBA8888, fbWidth,
		    		fbHeight, hasDepth);
		}
		frameBuffers.put(fbIdn, fb);
	}

	/**
	 * Creates a new Framebuffer with given params and no depth buffer.
	 * @param fbIdn - this framebuffer's identifier
	 * @param format - pixel format of this framebuffer
	 * @param fbWidth - desired width
	 * @param fbHeight - desired height
	 */
	public void createFB(String fbIdn, Format format, int fbWidth, int fbHeight) {
		createFB(fbIdn, format, fbWidth, fbHeight, false);
	}

	/**
	 * Creates a new Framebuffer with given params, pixel format of RGBA8888 and no depth buffer.
	 * @param fbIdn - this framebuffer's identifier
	 * @param fbWidth - desired width
	 * @param fbHeight - desired height
	 */
	public void createFB(String fbIdn, int fbWidth, int fbHeight) {
		createFB(fbIdn, Format.RGBA8888, fbWidth, fbHeight);
	}

	/**
	 * Creates a new Framebuffer with given params, screen resolution, pixel format of RGBA8888 and no depth buffer.
	 * @param fbIdn - this framebuffer's identifier
	 */
	public void createFB(String fbIdn) {
		createFB(fbIdn, Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	/**
	 * Creates a quad which spans entire screen, used for rendering of framebuffers.
	 */
	private void createScreenQuad() {
		if (screenQuad != null)
			return;
		screenQuad = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 3,
	        "a_position"), new VertexAttribute(Usage.Color, 4, "a_color"),
	        new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));

	    Vector3 vec0 = new Vector3(0, 0, 0);
	    screenCamera.unproject(vec0);
	    Vector3 vec1 = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
	    screenCamera.unproject(vec1);
	    screenQuad.setVertices(new float[]{vec0.x, vec0.y, 0, 1, 1, 1, 1, 0, 1,
	                vec1.x, vec0.y, 0, 1, 1, 1, 1, 1, 1,
	                vec1.x, vec1.y, 0, 1, 1, 1, 1, 1, 0,
	                vec0.x, vec1.y, 0, 1, 1, 1, 1, 0, 0});
		screenQuad.setIndices(new short[]{0, 1, 2, 2, 3, 0});
	}

	/**
	 * Call this to start rendering using given shader.
	 * @param shadIdn - a shader with this identifier must be loaded earlier
	 * @return this ShaderProgram for chaining
	 */
	public ShaderProgram begin(String shadIdn) {
		if (currentShader != null)
			throw new IllegalArgumentException("Previous shader '" + currentShaderIdn + "' not finished! Call end() before another begin().");

		ShaderProgram res = get(shadIdn);
		if (res != null) {
			currentShader = res;
			currentShaderIdn = shadIdn;
			currentTextureId = 0;
			res.begin();
		}
		else {
			throw new IllegalArgumentException("Shader '" + shadIdn + "' not found!");
		}
		return res;
	}

	/**
	 * Call this to finish rendering using current shader.
	 */
	public void end() {
		if (currentShader != null) {
			currentShader.end();
			currentShader = null;
			currentShaderIdn = null;
		}
	}

	/**
	 * Call this to start rendering to given framebuffer.
	 * @param fb - framebuffer to render to
	 * @param clearColor - clear color for this framebuffer
	 */
	private void beginFB(FrameBuffer fb, Color clearColor) {
		if (fb == null) {
			throw new IllegalArgumentException("FrameBuffer must not be null!");
		}
        fb.begin();
        Gdx.graphics.getGL20().glViewport(0, 0, fb.getWidth(), fb.getHeight());
        Gdx.graphics.getGL20().glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT
                | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA,
                GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Call this to start rendering to given framebuffer.
	 * @param fbIdn - framebuffer identifier to render to
	 * @param clearColor - clear color for this framebuffer
     */
    public void beginFB(String fbIdn, Color clearColor) {
    	beginFB(frameBuffers.get(fbIdn), clearColor);
    	openedFrameBuffers.add(fbIdn);
    }

    /**
     * Call this to start rendering to given framebuffer, using a BLACK clearing color.
	 * @param fbIdn - framebuffer identifier to render to
     */
    public void beginFB(String fbIdn) {
    	beginFB(fbIdn, Color.BLACK);
    }

    private void endFB(FrameBuffer fb) {
    	if (fb == null) {
			throw new IllegalArgumentException("FrameBuffer must not be null!");
		}
        fb.end();
    }

    /**
     * Call this to finish rendering into current framebuffer.
     */
    public void endFB() {
    	if (openedFrameBuffers.size == 0) {
			throw new IllegalArgumentException("No framebuffers to end!");
		}
    	endFB(frameBuffers.get(openedFrameBuffers.pop()));
    }

    /**
	 * Renders given framebuffer onto given Mesh.
	 * @param fb - FrameBuffer to render
	 * @param out - a Mesh to render the framebuffer to
	 * @param textureUniformName - the name of the texture2d uniform parameter in the fragment shader
	 * @param textureId - which texture number should be used
	 */
	public void renderFB(FrameBuffer fb, Mesh out, String textureUniformName, int textureId) {
		if (fb != null) {
			fb.getColorBufferTexture().bind(textureId);
	        getCurrent().setUniformMatrix("u_worldView", screenCamera.combined);
	        getCurrent().setUniformi(textureUniformName, textureId);
	        out.render(getCurrent(), GL20.GL_TRIANGLES);
		}
	}

	/**
	 * Renders given framebuffer onto given Mesh.
	 * @param fbIdn
	 * @param out - a Mesh to render the framebuffer to
	 * @param textureUniformName - the name of the texture2d uniform parameter in the fragment shader
	 * @param textureId - which texture number should be used
	 */
	public void renderFB(String fbIdn, Mesh out, String textureUniformName, int textureId) {
		FrameBuffer fb = frameBuffers.get(fbIdn);
		if (fb != null) {
			renderFB(fb, out, textureUniformName, textureId);
		}
	}

	/**
	 * Renders given framebuffer onto given Mesh.
	 * @param fb - FrameBuffer to render
	 * @param out - a Mesh to render the framebuffer to
	 */
	public void renderFB(FrameBuffer fb, Mesh out) {
		renderFB(fb, out, "u_texture", FRAMEBUFFER_TEXTURE_ID);
	}

	/**
	 * Renders given framebuffer onto given Mesh.
	 * @param fbIdn
	 * @param out - a Mesh to render the framebuffer to
	 */
	public void renderFB(String fbIdn, Mesh out) {
		renderFB(fbIdn, out, "u_texture", FRAMEBUFFER_TEXTURE_ID);
	}

	/**
	 * Renders given framebuffer into entire screen.
	 * @param fb - FrameBuffer
	 * @param textureUniformName - the name of the texture2d uniform parameter in the fragment shader
	 * @param textureId - which texture number should be used
	 */
	public void renderFB(FrameBuffer fb, String textureUniformName, int textureId) {
		renderFB(fb, screenQuad, textureUniformName, textureId);
	}

	/**
	 * Renders given framebuffer into entire screen.
	 * @param fbIdn
	 * @param textureUniformName - the name of the texture2d uniform parameter in the fragment shader
	 * @param textureId - which texture number should be used
	 */
	public void renderFB(String fbIdn, String textureUniformName, int textureId) {
		renderFB(fbIdn, screenQuad, textureUniformName, textureId);
	}

	/**
	 * Renders given framebuffer into entire screen.
	 * @param fb - FrameBuffer to render
	 */
	public void renderFB(FrameBuffer fb) {
		renderFB(fb, screenQuad, "u_texture", FRAMEBUFFER_TEXTURE_ID);
	}

	/**
	 * Renders given framebuffer into entire screen.
	 * @param fbIdn
	 */
	public void renderFB(String fbIdn) {
		renderFB(fbIdn, screenQuad, "u_texture", FRAMEBUFFER_TEXTURE_ID);
	}

	/**
	 * Renders given framebuffer into entire screen using default shader.
	 * @param fb - FrameBuffer to render
	 */
	public void renderFBDefault(FrameBuffer fb) {
		begin("default");
		renderFB(fb, screenQuad, "u_texture", FRAMEBUFFER_TEXTURE_ID);
		end();
	}

	/**
	 * Renders given framebuffer into entire screen using default shader.
	 * @param fbIdn
	 */
	public void renderFBDefault(String fbIdn) {
		begin("default");
		renderFB(fbIdn, screenQuad, "u_texture", FRAMEBUFFER_TEXTURE_ID);
		end();
	}

	/**
	 * @return are we rendering to a framebuffer now?
	 */
	public boolean isRenderingToFB() {
		if (currentShader != null && frameBuffers.containsKey(currentShaderIdn))
			return true;
		return false;
	}

	/**
	 * Sets given framebuffer.
	 * @param fbIdn
	 * @param framebuffer to set
	 */
	public void setFB(String fbIdn, FrameBuffer fb) {
		frameBuffers.put(fbIdn, fb);
	}

	/**
	 * Gets requested framebuffer.
	 * @param fbIdn
	 * @return
	 */
	public FrameBuffer getFB(String fbIdn) {
		FrameBuffer fb = frameBuffers.get(fbIdn);
		if (fb != null) {
			return fb;
		}
		return null;
	}

	/**
	 * Gets requested framebuffer's texture.
	 * @param fbIdn
	 * @return
	 */
	public Texture getFBTexture(String fbIdn) {
		FrameBuffer fb = frameBuffers.get(fbIdn);
		if (fb != null) {
			return fb.getColorBufferTexture();
		}
		return null;
	}

	/**
	 * @return current (free) texture id
	 */
	public int getCurrentTextureId() {
		return currentTextureId++;
	}

	/**
	 * Resizes internal camera for framebuffer use, call this in you ApplicationListener's resize.
	 * @param width - new screen width
	 * @param height - new screen height
	 * @param resizeFramebuffers - whether all of the framebuffers should be recreated to match new screen size
	 */
	public void resize(int width, int height, boolean resizeFramebuffers) {
		//?????
		if (resizeFramebuffers) {
			Keys keys = frameBuffers.keys();
			while (keys.hasNext) {
				String key = (String) keys.next();
				FrameBuffer fb = frameBuffers.get(key);
				int oldWidth = fb.getWidth();
				int oldHeight = fb.getHeight();
				Format format = fb.getColorBufferTexture().getTextureData().getFormat();
				fb.dispose();
				frameBuffers.put(key, null);
				float factorX = 1f*width/screenCamera.viewportWidth;
				float factorY = 1f*height/screenCamera.viewportHeight;
				createFB(key, format, (int) (factorX * oldWidth), (int) (factorY * oldHeight));
				//System.out.println("Recreated FB '" + key + "' from " + oldWidth + "x" + oldHeight + " to " + frameBuffers.get(key).getWidth() + "x" + frameBuffers.get(key).getHeight());
			}
		}

		screenCamera = new OrthographicCamera(width, height);
		createScreenQuad();
	}

	/**
	 * Resizes internal camera for framebuffer use, call this in you ApplicationListener's resize.
	 * @param width - new screen width
	 * @param height - new screen height
	 */
	public void resize(int width, int height) {
		resize(width, height, false);
	}

	/**
	 * Dispose this ShaderManager with all shaders and framebuffers
	 */
	public void dispose() {
		for (ShaderProgram sp: shaders.values()) {
			sp.dispose();
		}
		for (FrameBuffer fb: frameBuffers.values()) {
			fb.dispose();
		}
		shaders.clear();
		frameBuffers.clear();
	}

	public void disposeFB(String fbIdn) {
		FrameBuffer fb = frameBuffers.get(fbIdn);
		if (fb != null) {
			fb.dispose();
			frameBuffers.remove(fbIdn);
		}
	}

	public void setAssetManager(AssetManager amm) {
		this.am = amm;
	}

	/**
	 * Add new shader to ShaderManager
	 * @param fh
	 */
	@Deprecated
	public void add(FileHandle fh) {
		String key = fh.nameWithoutExtension();
		String frag = null, vert = null;
		String vertPath = null, fragPath = null;
		if (fh.extension().endsWith("frag")) {
			fh = fixPath(fh);
			frag = fh.readString("utf-8");
			fragPath = fh.path();
			FileHandle fh2 = Gdx.files.internal(fh.parent().path() + "/" + key
					+ ".vert");
			if (fh2.exists()) {
				vert = fh2.readString("utf-8");
				vertPath = fh2.path();
			}
		} else if (fh.extension().endsWith("vert")) {
			fh = fixPath(fh);
			vert = fh.readString("utf-8");
			vertPath = fh.path();
			FileHandle fh2 = Gdx.files.internal(fh.parent().path() + "/" + key
					+ ".frag");
			if (fh2.exists()) {
				frag = fh2.readString("utf-8");
				fragPath = fh2.path();
			}
		}
		if (frag == null || vert == null)
			return;
		frag = appendGLESPrecisions(frag);
		vert = appendGLESPrecisions(vert);
		sourcesVert.put(key, vert);
		sourcesFrag.put(key, frag);
		ShaderProgram sp = new ShaderProgram(vert, frag);
		if (!sp.isCompiled()) {
			Gdx.app.log("ShaderManager", "Error while loading shader '" + key
					+ "':\n" + sp.getLog());
			Gdx.app.log("ShaderManager", "--------------------------------");
			Gdx.app.log("ShaderManager", "Vertex shader source: \n" + vert);
			Gdx.app.log("ShaderManager", "--------------------------------");
			Gdx.app.log("ShaderManager", "Fragment shader source: \n" + frag);
			Gdx.app.log("ShaderManager", "--------------------------------");
			return;
		}
		shaders.put(key, sp);
		shaderPaths.put(key, vertPath + ";" + fragPath);
		Gdx.app.log("ShaderManager", "Shader '" + key + "' loaded");
	}

	/**
	 * Add new shader to ShaderManager
	 * @param filePath
	 */
	@Deprecated
	public void add(String filePath) {
		add(Gdx.files.internal(filePath));
	}

	/**
	 * Adds new shader to ShaderManager.
	 * @param key - shader identifier
	 * @param fh - FileHandle to vertex shader source
	 * @param fh2 - FileHandle to fragment shader source
	 */
	public void add(String key, FileHandle fh, FileHandle fh2) {
		String frag = null, vert = null;
		fh = fixPath(fh);
		fh2 = fixPath(fh2);
		vert = fh.readString("utf-8");
		frag = fh2.readString("utf-8");
		if (init(key, vert, frag)) {
			shaderPaths.put(key, fh.path() + ";" + fh2.path());
		}
	}

	/**
	 * Adds new shader to ShaderManager.
	 * @param key - shader identifier
	 * @param vertPath - path to vertex shader source
	 * @param fragPath - path to fragment shader source
	 */
	public void add(String key, String vertPath, String fragPath) {
		if (am != null)
			add(am, key, vertPath, fragPath);
		else
			add(key, Gdx.files.internal(vertPath), Gdx.files.internal(fragPath));
	}

	/**
	 * Adds new shader to ShaderManager, loads it using AssetManager.
	 * @param am - AssetManager instance
	 * @param key - shader identifier
	 * @param baseVertPath - path to vertex shader source
	 * @param baseFragPath - path to fragment shader source
	 */
	public void add(AssetManager am, String key, String baseVertPath, String baseFragPath) {
		am.setLoader(String.class, new TextFileLoader(new InternalFileHandleResolver()));

		FileHandle vertFh = null, fragFh = null;
		String vertPath = shaderDir + "/" + baseVertPath;
		vertFh = Gdx.files.internal(vertPath);
		if (!vertFh.exists()) {
			vertFh = Gdx.files.classpath(SHADER_CLASSPATH + "/" + baseVertPath);
			vertPath = vertFh.path();
		}
		String fragPath = shaderDir + "/" + baseFragPath;
		fragFh = Gdx.files.internal(fragPath);
		if (!fragFh.exists()) {
			fragFh = Gdx.files.classpath(SHADER_CLASSPATH + "/" + baseFragPath);
			fragPath = fragFh.path();
		}

        if (!vertFh.file().exists()) // added .file()
			throw new GdxRuntimeException("ShaderManager: shader '" + vertPath + "' does not exist!");
		if (!fragFh.file().exists()) // added .file()
			throw new GdxRuntimeException("ShaderManager: shader '" + fragPath + "' does not exist!");

		if (am.isLoaded(vertPath))
			am.unload(vertPath);
		if (am.isLoaded(fragPath))
			am.unload(fragPath);

		am.load(vertPath, String.class);
		am.load(fragPath, String.class);

		//TODO dirty...
		while (!am.isLoaded(vertPath) || !am.isLoaded(fragPath)) {
			am.update();
			//Gdx.app.log("ShaderManager", am.getProgress() + ", " + am.getLoadedAssets() + "/" + am.getQueuedAssets());
		}
		String vert = am.get(vertPath, String.class);
		String frag = am.get(fragPath, String.class);
		if (init(key, vert, frag)) {
			shaderPaths.put(key, baseVertPath + ";" + baseFragPath);
		}
	}

	/**
	 * Adds GLES specifics (if needed) and compiles shaders.
	 * @param key - shader identifier
	 * @param vert - vertex shader source
	 * @param frag - fragment shader source
	 * @return whether the shaders compiled correctly
	 */
	protected boolean init(String key, String vert, String frag) {
		if (frag == null || vert == null)
			return false;
		frag = appendGLESPrecisions(frag);
		vert = appendGLESPrecisions(vert);
		sourcesVert.put(key, vert);
		sourcesFrag.put(key, frag);
		ShaderProgram sp = new ShaderProgram(vert, frag);
		if (!sp.isCompiled()) {
			Gdx.app.log("ShaderManager", "Error while loading shader '" + key
					+ "':\n" + sp.getLog());
			Gdx.app.log("ShaderManager", "--------------------------------");
			Gdx.app.log("ShaderManager", "Vertex shader source: \n" + vert);
			Gdx.app.log("ShaderManager", "--------------------------------");
			Gdx.app.log("ShaderManager", "Fragment shader source: \n" + frag);
			Gdx.app.log("ShaderManager", "--------------------------------");
			return false;
		}
		shaders.put(key, sp);
		Gdx.app.log("ShaderManager", "Shader '" + key + "' loaded");
		return true;
	}

	private FileHandle fixPath(FileHandle fh) {
		if (fh == null)
			return null;
		if (!fh.exists()) {
			fh = Gdx.files.internal(shaderDir + "/" + fh.path());
		}
		if (!fh.exists()) {
			throw new GdxRuntimeException("Shader not found: " + fh.path());
		}
		return fh;
	}

	private String appendGLESPrecisions(String shader, String prec) {
		if (shader == null)
			return null;
		if (Gdx.app.getType() == ApplicationType.Desktop || Gdx.app.getType() == ApplicationType.Applet)
			return shader;
		String tmp = "#ifdef GL_ES\n " + "precision " + prec + " float;\n"
				+ "precision " + prec + " int;\n" +
				// "precision " + prec + " vec2;\n" +
				// "precision " + prec + " vec3;\n" +
				// "precision " + prec + " vec4;\n" +
				"#endif\n\n";
		shader = shader.replace("vec2 ", prec + " vec2 ");
		shader = shader.replace("vec3 ", prec + " vec3 ");
		shader = shader.replace("vec4 ", prec + " vec4 ");
		return tmp + shader;
	}

	private String appendGLESPrecisions(String shader) {
		return appendGLESPrecisions(shader, "mediump");
	}

	/**
	 * Returns given shader from ShaderManager.
	 * @param name - shader identifier
	 * @return requested shader
	 */
	public ShaderProgram get(String name) {
		if (shaders.containsKey(name))
			return shaders.get(name);
		throw new GdxRuntimeException("No shader named '" + name + "' in ShaderManager!");
	}

	/**
	 * Returns current shader from ShaderManager.
	 * @return requested shader
	 */
	public ShaderProgram getCurrent() {
		if (currentShader != null)
			return currentShader;
		throw new GdxRuntimeException("No current shader set in ShaderManager!");
	}

	/**
	 * Returns the vertex shader source for requested shader
	 * @param name - shader identifier
	 * @return vertex shader source
	 */
	public String getSourceVert(String name) {
		if (sourcesVert.containsKey(name))
			return sourcesVert.get(name);
		return null;
	}

	/**
	 * Returns the fragment shader source for requested shader
	 * @param name - shader identifier
	 * @return fragment shader source
	 */
	public String getSourceFrag(String name) {
		if (sourcesFrag.containsKey(name))
			return sourcesFrag.get(name);
		return null;
	}

	/**
	 * Reloads all shaders from disk. Useful for writing shaders: edit and save shader source, reload in game.
	 * Should be used for development purposes only!
	 */
	public void reload() {
		float t = System.currentTimeMillis();
		Keys keys = shaderPaths.keys();
		while (keys.hasNext) {
			String key = (String) keys.next();
			int ind = shaderPaths.get(key).indexOf(";");
			String vertPath = shaderPaths.get(key).substring(0, ind);
			String fragPath = shaderPaths.get(key).substring(ind + 1,
					shaderPaths.get(key).length());
			if (am != null)
				add(am, key, vertPath, fragPath);
			else
				add(key, vertPath, fragPath);
		}
		Gdx.app.log("ShaderManager", "Shaders reloaded in " + (System.currentTimeMillis() - t) + "ms");
	}



	/* passing uniforms to current shader */

	/** Sets the given attribute
	 *
	 * @param name the name of the attribute
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value
	 * @param value4 the fourth value */
	public ShaderProgram setAttributef(String name, float value1, float value2,
			float value3, float value4) {
		if (currentShader != null) {
			currentShader.setAttributef(name, value1, value2, value3, value4);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform1fv(String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform1fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform2fv(String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform2fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform3fv(String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform3fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	public ShaderProgram setUniform4fv(String name, float[] values, int offset,
			int length) {
		if (currentShader != null) {
			currentShader.setUniform4fv(name, values, offset, length);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value the value */
	public ShaderProgram setUniformf(String name, float value) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value */
	public ShaderProgram setUniformf(String name, float value1, float value2) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value1, value2);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value */
	public ShaderProgram setUniformf(String name, float value1, float value2,
			float value3) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value1, value2, value3);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value
	 * @param value4 the fourth value */
	public ShaderProgram setUniformf(String name, float value1, float value2,
			float value3, float value4) {
		if (currentShader != null) {
			currentShader.setUniformf(name, value1, value2, value3, value4);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value the value */
	public ShaderProgram setUniformi(String name, int value) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value */
	public ShaderProgram setUniformi(String name, int value1, int value2) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value1, value2);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value */
	public ShaderProgram setUniformi(String name, int value1, int value2, int value3) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value1, value2, value3);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param value1 the first value
	 * @param value2 the second value
	 * @param value3 the third value
	 * @param value4 the fourth value */
	public ShaderProgram setUniformi(String name, int value1, int value2, int value3,
			int value4) {
		if (currentShader != null) {
			currentShader.setUniformi(name, value1, value2, value3, value4);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param matrix the matrix */
	public ShaderProgram setUniformMatrix(String name, Matrix3 matrix) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param matrix the matrix
	 * @param transpose whether the uniform matrix should be transposed */
	public ShaderProgram setUniformMatrix(String name, Matrix3 matrix,
			boolean transpose) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix, transpose);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param matrix the matrix */
	public ShaderProgram setUniformMatrix(String name, Matrix4 matrix) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform matrix with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform
	 * @param matrix the matrix
	 * @param transpose whether the matrix should be transposed */
	public ShaderProgram setUniformMatrix(String name, Matrix4 matrix,
			boolean transpose) {
		if (currentShader != null) {
			currentShader.setUniformMatrix(name, matrix, transpose);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/** Sets the uniform with the given name, automatically binding the texture to a number. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the name of the uniform */
	public ShaderProgram setUniformTexture(String name, Texture value) {
		if (currentShader != null) {
			int texId = getCurrentTextureId();
			value.bind(texId);
			currentShader.setUniformi(name, texId);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

	/*
	 * public void setVertexAttribute(java.lang.String name, int size, int type,
	 * boolean normalize, int stride, java.nio.Buffer buffer) { if
	 * (currentShader != null) { currentShader.setVertexAttribute(name, size,
	 * type, normalize, stride, buffer); } else throw new
	 * IllegalArgumentException("Can't set uniform before calling begin()!"); }
	 */


	/** Sets the vertex attribute with the given name. Throws an IllegalArgumentException in case it is not called in between a
	 * {@link #begin()}/{@link #end()} block.
	 *
	 * @param name the attribute name
	 * @param size the number of components, must be >= 1 and <= 4
	 * @param type the type, must be one of GL20.GL_BYTE, GL20.GL_UNSIGNED_BYTE, GL20.GL_SHORT,
	 *           GL20.GL_UNSIGNED_SHORT,GL20.GL_FIXED, or GL20.GL_FLOAT. GL_FIXED will not work on the desktop
	 * @param normalize whether fixed point data should be normalized. Will not work on the desktop
	 * @param stride the stride in bytes between successive attributes
	 * @param offset byte offset into the vertex buffer object bound to GL20.GL_ARRAY_BUFFER. */
	public ShaderProgram setVertexAttribute(String name, int size, int type,
			boolean normalize, int stride, int offset) {
		if (currentShader != null) {
			currentShader.setVertexAttribute(name, size, type, normalize,
					stride, offset);
			return currentShader;
		} else
			throw new IllegalArgumentException(
					"Can't set uniform before calling begin()!");
	}

}
