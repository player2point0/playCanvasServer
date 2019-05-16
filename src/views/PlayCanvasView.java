package views;

import org.json.JSONObject;

import animation.Animate;
import mathematics.GeneralMatrixFloat;
import mathematics.GeneralMatrixObject;
import mathematics.GeneralMatrixString;
import meshprocessing.MeshEdges;
import procedural.human.HumanBody;
import procedural.human.HumanVolume;
import procedural.human.resources.Bones;
import procedural.human.resources.Sknb;
import procedural.human.resources.Sknw;
import procedural.human.resources.makehuman.QuadMesh;
import procedural.human.resources.makehuman.QuadUvs;
import procedural.human.resources.makehuman.Uvs;
import procedural.primitives.Mesh;
import procedural.primitives.MeshConnectivity;
import procedural.primitives.MeshMirror;
import procedural.primitives.Skeleton;
import procedural.primitives.Skin;
import procedural.primitives.SkinnedVolume;
import rendering.RenderBuffer;
import rendering.shaders.constructed.MeshRasteriser3DUV;

import org.json.JSONArray;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class PlayCanvasView extends DynamicWebPage {
	
    public PlayCanvasView(DatabaseInterface db, FileStoreInterface fs) {
		super(db, fs);
		// TODO Auto-generated constructor stub
	}
    
    public boolean process(WebRequest toProcess)
    {
        if(toProcess.path.equalsIgnoreCase("playCanvas"))
        {
            String stringToSendToWebBrowser = "<html>\n" + 
        			"<head>"+
            			"<script src=\"https://code.playcanvas.com/playcanvas-latest.js\"></script>\n" + 
            			"<script src=\"./js/JavaEntity.js\"></script>\n" + 
            		"</head>"+
            		"<body>\n" + 
            		"<canvas id=\"application-canvas\"></canvas>\n" + 
            		"<script src=\"js/playCanvas.js\"></script>\n"+
            		"  </body>\n" + 
            		"</html>";

            toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
            return true;
        }

        else if(toProcess.path.equalsIgnoreCase("playCanvasStart"))
        {
        	/*
        	GeneralMatrixObject skeletons = new GeneralMatrixObject(1);
        	GeneralMatrixObject skins = new GeneralMatrixObject(1);
        	GeneralMatrixObject volumes = new GeneralMatrixObject(1);
			Mesh trimesh = new Mesh();

			GeneralMatrixString morphnames = new GeneralMatrixString(1);
			GeneralMatrixFloat morphmagnitudes = new GeneralMatrixFloat(1);
			
			morphnames.push_back("NeutralMaleChild");
			//morphnames.push_back("NeutralFemaleYoung");
			morphmagnitudes.push_back(1.0f);
			
			Skeleton skel = new Skeleton();
			Skin skin = new Skin();
			SkinnedVolume svol = new SkinnedVolume();
			
			skel.boneParents.setDimensions(1, Bones.names.length);
			skel.boneParents.set(Bones.bprnts);
			skel.boneJoints.setDimensions(2,Bones.names.length);
			skel.boneJoints.set(Bones.bones);
			HumanBody.createMorphedBody(morphnames,morphmagnitudes, skin.bpos, 
					skel.vpos,skel.bmats,skel.bonelengths,skel.localbindbmats);

			skeletons.push_back(skel);
			skins.push_back(skin);
			volumes.push_back(svol);
						
			skel.lpos.setDimensions(3,1+(Bones.bones.length/2));
			
			trimesh.pos.setDimensions(3, skin.bpos.height);

			skin.sb = Sknb.get();
			skin.sw = Sknw.get();
			HumanVolume.bonemapping(skin.bpos, skel.vpos, skel.boneJoints, skin.sb, skin.sw, svol.pbone, svol.pbextent);
			
			skel.tvpos.setDimensions(skel.vpos.width,skel.vpos.height);
			skel.tbmats.setDimensions(skel.bmats.width,skel.bmats.height);

	    	int[] quads = QuadMesh.get();
			trimesh.quads.setDimensions(4,quads.length/4);
			trimesh.quads.set(quads);
			int[] quvs = QuadUvs.get();
			trimesh.quaduvs.setDimensions(4,quads.length/4);
			trimesh.quaduvs.set(quvs);
			float[] uvs = Uvs.get();
			trimesh.uvs.setDimensions(2, uvs.length/2);
			trimesh.uvs.set(uvs);
			trimesh.quadnrms.setDimensions(4, quads.length/4);
			
        	double[] meshPointsArr = new double[trimesh.pos.height * 3];
        	int[] meshIndicesArr = new int[trimesh.quads.height * 6];
        	double[] meshUvsArr = new double[trimesh.quads.height * 6];
        	
        	Animate.transformWithParams(skel.boneJoints.value, skel.boneParents.value,
					skel.bonelengths, skel.localbindbmats,
					skel.tvpos, skel.tbmats, skel.lpos);

	    	Animate.updateSkinUsingSkeleton(skel.tvpos, skel.tbmats, skel.vpos, skel.bmats, skin.bpos, Bones.bones, skin.sb, skin.sw, trimesh.pos);
			

        	//line 847 renderTris()				
			int j = 0;	
			
			for(int qi=0;qi<trimesh.quads.height;qi++)
			{	
				int v0 = trimesh.quads.value[qi*trimesh.quads.width+0];
				int v1 = trimesh.quads.value[qi*trimesh.quads.width+1];
				int v2 = trimesh.quads.value[qi*trimesh.quads.width+2];
				int v3 = trimesh.quads.value[qi*trimesh.quads.width+3];

				int uv0 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+0];
				int uv1 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+1];
				int uv2 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+2];
				int uv3 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+3];

				meshIndicesArr[j] = v0;
				meshIndicesArr[j+1] = v1;
				meshIndicesArr[j+2] = v2;
				meshIndicesArr[j+3] = v0;
				meshIndicesArr[j+4] = v2;
				meshIndicesArr[j+5] = v3;
				
				meshUvsArr[j] = uv0;
				meshUvsArr[j+1] = uv1;
				meshUvsArr[j+2] = uv2;
				meshUvsArr[j+3] = uv0;
				meshUvsArr[j+4] = uv2;
				meshUvsArr[j+5] = uv3;
				
				j+=6;			
			}
    				
        	
        	for(int i = 0;i<trimesh.pos.height;i++)
        	{
        		meshPointsArr[i*3+0] = trimesh.pos.value[i*3];
        		meshPointsArr[i*3+1] = trimesh.pos.value[(i*3)+1];
        		meshPointsArr[i*3+2] = trimesh.pos.value[(i*3)+2];        	
        	}
        	*/
            JSONObject responseData = new JSONObject();
            JSONArray entities = new JSONArray();
            
            double[] normalsArr = new double[] {};
            double[] uvsArr = new double[] {};
            
            double[] meshPointsArr = new double[] {1, 1, 0,
            		-1, 1, 0,
            		-1, -1, 0,
            		1, -1, 0};
            int[] meshIndicesArr = new int[] { 0, 1, 2, 0, 2, 3};
            
            JSONObject entity1 = makeEntity(meshPointsArr, uvsArr, meshIndicesArr, "asset", "box1", 0, 0, 0, false, "data:image/png;base64,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/GMzLdsAAAAgY0hSTQAAeiUAAICDAAD5/wAAgOkAAHUwAADqYAAAOpgAABdvkl/FRgAAOqlJREFUeNrs3XmcFPWd+P/6fOruY5hhhvu+RVAOwQMREG8RUbkGk03WJG6yZnN4RRQ8QNSYxGSzZnP5NYmggIL3gUZBxIsoKpciCDPDzfScPUdPX1X1+4NNfmGmEzDpmtg9r+df+9hHQuXx7up+TU1PvUvUf2OQAgAAsk0yAgAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAASCwAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQUAACQWAAASCwAAiQX+NULzn1D7jWQOAEgskGXqoLGhBU/bX71PFJQwDQAkFsgqIYyJs8NLXjMv+oai6cwDAIkFstpZO2TNmh9etEY79VymAYDEAtk+U7v2D37noeD3fie7D2IaAEgskGXayEnhRS9ZcxYIO8w0AJBYILvnrGpecE34nrXGpFJFcAIDILFAVolwZ/vfloRuf1YbOp5pACCxQJapfYYHb14R+I+fy849mQYAEgtkmT5+WmjJH63Lvyt0i2kAILFANgndMqd/N7TkVX38NKYBgMQC2T6bO/cI/MfPgz9YofYZzjQAkFggy7Qh40O3P2f/2xIRLmYaAEgskFVCGJNK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\"");            
            entities.put(0, entity1);
            responseData.put("entities", entities);
                    
            responseData.put("time", System.currentTimeMillis()); 
                        
            toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_PLAINTEXT, responseData.toString() );
            return true;
        }
        
        else if(toProcess.path.equalsIgnoreCase("playCanvasUpdate"))
        {  	
        	GeneralMatrixObject skeletons = new GeneralMatrixObject(1);
        	GeneralMatrixObject skins = new GeneralMatrixObject(1);
        	GeneralMatrixObject volumes = new GeneralMatrixObject(1);
			Mesh trimesh = new Mesh();

			GeneralMatrixString morphnames = new GeneralMatrixString(1);
			GeneralMatrixFloat morphmagnitudes = new GeneralMatrixFloat(1);
			
			morphnames.push_back("NeutralMaleChild");
			//morphnames.push_back("NeutralFemaleYoung");
			morphmagnitudes.push_back(1.0f);
			
			Skeleton skel = new Skeleton();
			Skin skin = new Skin();
			SkinnedVolume svol = new SkinnedVolume();
			
			skel.boneParents.setDimensions(1, Bones.names.length);
			skel.boneParents.set(Bones.bprnts);
			skel.boneJoints.setDimensions(2,Bones.names.length);
			skel.boneJoints.set(Bones.bones);
			HumanBody.createMorphedBody(morphnames,morphmagnitudes, skin.bpos, 
					skel.vpos,skel.bmats,skel.bonelengths,skel.localbindbmats);

			skeletons.push_back(skel);
			skins.push_back(skin);
			volumes.push_back(svol);
						
			skel.lpos.setDimensions(3,1+(Bones.bones.length/2));
			
			trimesh.pos.setDimensions(3, skin.bpos.height);

			skin.sb = Sknb.get();
			skin.sw = Sknw.get();
			HumanVolume.bonemapping(skin.bpos, skel.vpos, skel.boneJoints, skin.sb, skin.sw, svol.pbone, svol.pbextent);
			
			skel.tvpos.setDimensions(skel.vpos.width,skel.vpos.height);
			skel.tbmats.setDimensions(skel.bmats.width,skel.bmats.height);

	    	int[] quads = QuadMesh.get();
			trimesh.quads.setDimensions(4,quads.length/4);
			trimesh.quads.set(quads);
			int[] quvs = QuadUvs.get();
			trimesh.quaduvs.setDimensions(4,quads.length/4);
			trimesh.quaduvs.set(quvs);
			float[] uvs = Uvs.get();
			trimesh.uvs.setDimensions(2, uvs.length/2);
			trimesh.uvs.set(uvs);
			trimesh.quadnrms.setDimensions(4, quads.length/4);
			
        	double[] meshPointsArr = new double[trimesh.pos.height * 3];
        	int[] meshIndicesArr = new int[trimesh.quads.height * 6];
        	double[] meshUvsArr = new double[trimesh.quads.height * 6];
        	
        	Animate.transformWithParams(skel.boneJoints.value, skel.boneParents.value,
					skel.bonelengths, skel.localbindbmats,
					skel.tvpos, skel.tbmats, skel.lpos);

	    	Animate.updateSkinUsingSkeleton(skel.tvpos, skel.tbmats, skel.vpos, skel.bmats, skin.bpos, Bones.bones, skin.sb, skin.sw, trimesh.pos);
			

        	//line 847 renderTris()				
			int j = 0;	
			
			for(int qi=0;qi<trimesh.quads.height;qi++)
			{	
				int v0 = trimesh.quads.value[qi*trimesh.quads.width+0];
				int v1 = trimesh.quads.value[qi*trimesh.quads.width+1];
				int v2 = trimesh.quads.value[qi*trimesh.quads.width+2];
				int v3 = trimesh.quads.value[qi*trimesh.quads.width+3];

				int uv0 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+0];
				int uv1 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+1];
				int uv2 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+2];
				int uv3 = trimesh.quaduvs.value[qi*trimesh.quaduvs.width+3];

				meshIndicesArr[j] = v0;
				meshIndicesArr[j+1] = v1;
				meshIndicesArr[j+2] = v2;
				meshIndicesArr[j+3] = v0;
				meshIndicesArr[j+4] = v2;
				meshIndicesArr[j+5] = v3;
				
				meshUvsArr[j] = uv0;
				meshUvsArr[j+1] = uv1;
				meshUvsArr[j+2] = uv2;
				meshUvsArr[j+3] = uv0;
				meshUvsArr[j+4] = uv2;
				meshUvsArr[j+5] = uv3;
				
				j+=6;			
			}
    				
        	
        	for(int i = 0;i<trimesh.pos.height;i++)
        	{
        		meshPointsArr[i*3+0] = trimesh.pos.value[i*3];
        		meshPointsArr[i*3+1] = trimesh.pos.value[(i*3)+1];
        		meshPointsArr[i*3+2] = trimesh.pos.value[(i*3)+2];        	
        	}
        		
            JSONObject responseData = new JSONObject();
            JSONArray entities = new JSONArray();
            
            double[] normalsArr = new double[] {};
            double[] uvsArr = new double[] {};

            JSONObject entity1 = makeEntity(meshPointsArr, uvsArr, meshIndicesArr, "box1");
  
            entities.put(0, entity1);
            responseData.put("entities", entities);
                    
            responseData.put("time", System.currentTimeMillis()); 
                        
            toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_PLAINTEXT, responseData.toString() );
            return true;
        }
         
        return false;
    }
    //update entity
    public JSONObject makeEntity(double[] positionsArr, double[] uvsArr, int[] indiciesArr, String name)
    {
    	 JSONObject entity = new JSONObject();
         JSONObject vertexData = new JSONObject(); 
         
         JSONArray positions = new JSONArray(positionsArr);     
         JSONArray uvs = new JSONArray(uvsArr);
         JSONArray indicies = new JSONArray(indiciesArr);
         
         vertexData.put("position", positions);
         vertexData.put("uvs", uvs);
         vertexData.put("indices", indicies);
         
         entity.put("vertexData", vertexData);
         entity.put("name", name);
         
         return entity;
    }
    //start entity
    public JSONObject makeEntity(double[] positionsArr, double[] uvsArr, int[] indiciesArr, String  model, String name, double x, double y, double z, boolean realTimeModel, String texture)
    {
    	 JSONObject entity = new JSONObject();
         JSONObject vertexData = new JSONObject(); 
         
         JSONArray positions = new JSONArray(positionsArr);     
         JSONArray uvs = new JSONArray(uvsArr);
         JSONArray indicies = new JSONArray(indiciesArr);
         
         vertexData.put("position", positions);
         vertexData.put("uvs", uvs);
         vertexData.put("indices", indicies);
         
         entity.put("model", model);
         entity.put("name", name);
         entity.put("x", x);
         entity.put("y", y);
         entity.put("z", z);
         entity.put("realtimeModel", realTimeModel);
         entity.put("vertexData", vertexData);
         entity.put("scriptName", "rotate1");
         entity.put("script", "this.entity.rotate(0, 10 * dt, 0);");
         entity.put("texture", texture);
         
         return entity;
    }
    
    public JSONArray randomCube()
    {
    	double[] points = new double[] {
    			// Front face
			  -1.0 * Math.random(), -1.0 * Math.random(),  1.0 * Math.random(),
			   1.0 * Math.random(), -1.0 * Math.random(),  1.0 * Math.random(),
			   1.0 * Math.random(),  1.0 * Math.random(),  1.0 * Math.random(),
			  -1.0 * Math.random(),  1.0 * Math.random(),  1.0 * Math.random(),
			  
			  // Back face
			  -1.0 * Math.random(), -1.0 * Math.random(), -1.0 * Math.random(),
			  -1.0 * Math.random(),  1.0 * Math.random(), -1.0 * Math.random(),
			   1.0 * Math.random(),  1.0 * Math.random(), -1.0 * Math.random(),
			   1.0, -1.0, -1.0,
			  
			  // Top face
			  -1.0 * Math.random(),  1.0 * Math.random(), -1.0 * Math.random(),
			  -1.0 * Math.random(),  1.0 * Math.random(),  1.0 * Math.random(),
			   1.0 * Math.random(),  1.0 * Math.random(),  1.0 * Math.random(),
			   1.0 * Math.random(),  1.0 * Math.random(), -1.0 * Math.random(),
			  
			  // Bottom face
			  -1.0 * Math.random(), -1.0 * Math.random(), -1.0 * Math.random(),
			   1.0 * Math.random(), -1.0 * Math.random(), -1.0 * Math.random(),
			   1.0 * Math.random(), -1.0 * Math.random(),  1.0 * Math.random(),
			  -1.0 * Math.random(), -1.0 * Math.random(),  1.0 * Math.random(),
			  
			  // Right face
			   1.0 * Math.random(), -1.0 * Math.random(), -1.0 * Math.random(),
			   1.0 * Math.random(),  1.0 * Math.random(), -1.0 * Math.random(),
			   1.0 * Math.random(),  1.0 * Math.random(),  1.0 * Math.random(),
			   1.0 * Math.random(), -1.0 * Math.random(),  1.0 * Math.random(),
			  
			  // Left face
			  -1.0 * Math.random(), -1.0 * Math.random(), -1.0 * Math.random(),
			  -1.0 * Math.random(), -1.0 * Math.random(),  1.0 * Math.random(),
			  -1.0 * Math.random(),  1.0 * Math.random(),  1.0 * Math.random(),
			  -1.0 * Math.random(),  1.0 * Math.random(), -1.0 * Math.random() };
    	
    	return new JSONArray(points);
    }
    

}