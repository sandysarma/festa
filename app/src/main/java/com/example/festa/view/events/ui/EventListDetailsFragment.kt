package com.example.festa.view.events.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageSwitcher
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festa.R
import com.example.festa.adapters.FeedAdapter
import com.example.festa.adapters.GalleryAdapter
import com.example.festa.adapters.PhotosAdapter
import com.example.festa.application.Festa
import com.example.festa.databinding.FragmentEventListDetailsBinding
import com.example.festa.models.Eventlist_Model
import com.example.festa.view.events.viewmodel.album.AllAlbumViewModel
import com.example.festa.view.events.viewmodel.createNewAlbum.CreateAlbumBody
import com.example.festa.view.events.viewmodel.createNewAlbum.CreateNewAlbumViewModel
import com.freqwency.promotr.utils.ErrorUtil
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class EventListDetailsFragment : Fragment() {
    private var pick_image: ImageSwitcher? = null
    private var mArrayUri: ArrayList<Uri>? = null

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    private val uri = ArrayList<Uri>()
    private var eventId = ""
    private lateinit var binding: FragmentEventListDetailsBinding

    private val allAlbumViewModel: AllAlbumViewModel by viewModels()
    private val createNewAlbumViewModel: CreateNewAlbumViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private lateinit var createAlbumBody: CreateAlbumBody

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEventListDetailsBinding.inflate(inflater, container, false)

        eventId = Festa.encryptedPrefs.eventIds
        Log.e("eventId", "eventId:  $eventId")

        /* val topLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
         binding.photorecycle.layoutManager = topLayoutManager
         binding.photorecycle.addItemDecoration(GridSpacingItemDecoration(3, dpToPx(), true))
         binding.photorecycle.itemAnimator = DefaultItemAnimator()
         binding.photorecycle.layoutManager = topLayoutManager*/

        binding.photorecycle.layoutManager =
            GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        val data = ArrayList<Eventlist_Model>()
        val photosAdapter = PhotosAdapter(data, requireActivity())
        binding.photorecycle.adapter = photosAdapter


        //............guestrecycle...............
        binding.feedrecycle.layoutManager = LinearLayoutManager(context)
        val data1 = ArrayList<Eventlist_Model>()
        val feedAdapter = FeedAdapter(requireContext(), data1)
        binding.feedrecycle.adapter = feedAdapter

        binding.feedbg.visibility = View.VISIBLE
        binding.feedbtn.setOnClickListener {
            binding.feedrecycle.visibility = View.VISIBLE
            binding.UploadImage.visibility = View.VISIBLE
            /*  val animation: Animation = TranslateAnimation(0f, 1000f, 0f, 0f)
              animation.duration = 1000*/
            //binding.chatandupdatepage.startAnimation(animation)
            //binding.addphotos.setVisibility(View.GONE)
            binding.photorecycle.visibility = View.GONE
            binding.createalbum.visibility = View.GONE

            binding.feedbg.visibility = View.VISIBLE
            binding.photobg.visibility = View.GONE
            binding.chatdbg.visibility = View.GONE
            binding.chatandupdatepage.visibility = View.GONE

            binding.photobtn.setBackgroundResource(0)
            binding.chatbtn.setBackgroundResource(0)
        }

        binding.chatbtn.setOnClickListener {
            binding.chatandupdatepage.visibility = View.VISIBLE
            val animation: Animation = TranslateAnimation(1000f, 0f, 0f, 0f)
            animation.duration = 500
            // binding.photorecycle.startAnimation(animation)
            //binding.addphotos.setVisibility(View.GONE)
            binding.UploadImage.visibility = View.GONE
            binding.feedrecycle.visibility = View.GONE
            binding.createalbum.visibility = View.GONE

            binding.feedbg.visibility = View.GONE
            binding.chatdbg.visibility = View.VISIBLE
            binding.photobg.visibility = View.GONE

            binding.photorecycle.visibility = View.GONE
            binding.feedbtn.setBackgroundResource(0)
            binding.photobtn.setBackgroundResource(0)
        }
        binding.photobtn.setOnClickListener {
            binding.photorecycle.visibility = View.VISIBLE
            binding.createalbum.visibility = View.VISIBLE

            binding.feedbg.visibility = View.GONE
            binding.chatdbg.visibility = View.GONE
            binding.photobg.visibility = View.VISIBLE

            binding.UploadImage.visibility = View.GONE
            binding.chatandupdatepage.visibility = View.GONE
            binding.chatbtn.setBackgroundResource(0)
            binding.feedbtn.setBackgroundResource(0)
        }

        //val picRecyclerView = findViewById<RecyclerView>(R.id.picimagerecycle1)
        //val pickImageBtn = view.findViewById<ImageView>(R.id.pickImageBtn1)

        Glide
            .with(requireActivity())
            .load(R.drawable.baseline_add_circle_outline_24)
            .into(binding.addImageBtn)

        Glide
            .with(requireActivity())
            .load(R.drawable.three_dot_icon)
            .into(binding.threeDotBtn)

        mArrayUri = ArrayList()
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Read_Permission
            )
        }

        binding.pickImageBtn.setOnClickListener {
            createAlbumDialog()

            //val intent = Intent()
            //intent.type = "image/*"
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }*/
            //intent.action = Intent.ACTION_GET_CONTENT
            // startActivityForResult(Intent.createChooser(intent, "select Picture"), 1)
        }
        allAlbumApi()
        allAlbumObserver()
        createNewAlbumObserver()
        return binding.root
    }

    /**
     * Show All Album
     */
    private fun allAlbumApi() {
        allAlbumViewModel.getAllAlbum(
            progressDialog,
            requireActivity(),
            eventId
        )
    }

    private fun allAlbumObserver() {
        allAlbumViewModel.progressIndicator.observe(viewLifecycleOwner) {
        }
        allAlbumViewModel.allAlbumResponse.observe(
            viewLifecycleOwner
        ) {
            val success = it.peekContent().success
            if (success!!) {
                val list = it.peekContent().albums
                binding.picimagerecycle1.isHorizontalScrollBarEnabled = true
                binding.picimagerecycle1.isHorizontalFadingEdgeEnabled = true
                //binding.rcvPromoter.layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
                binding.picimagerecycle1.layoutManager =
                    LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
                binding.picimagerecycle1.adapter = GalleryAdapter(requireActivity(), list)
            }
        }

        allAlbumViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }


    /**
     * Create New Album Dialog
     */
    @SuppressLint("MissingInflatedId")
    private fun createAlbumDialog() {
        val dialogView = layoutInflater.inflate(R.layout.create_new_album, null)
        val builder = AlertDialog.Builder(requireActivity()).setView(dialogView)
        val dialog = builder.create()
        val newAlbumTitle = dialogView.findViewById<TextView>(R.id.newAlbumTitle)
        val saveAlbumBtn = dialogView.findViewById<TextView>(R.id.saveAlbumBtn)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.setCancelable(true)

        saveAlbumBtn.setOnClickListener {
            val newAlbumTitles = newAlbumTitle.text.toString()

            if (newAlbumTitles.isEmpty()) {
                Toast.makeText(
                    requireActivity(), "Please enter album name", Toast.LENGTH_SHORT
                ).show()
            } else {
                createNewAlbumApi(newAlbumTitles)
                dialog.dismiss()
            }
        }
    }

    /**
     * Create New Album
     */
    private fun createNewAlbumApi(paramObject: String) {
        createAlbumBody = CreateAlbumBody(
            albumName = paramObject
        )
        createNewAlbumViewModel.createNewAlbums(
            progressDialog,
            requireActivity(),
            eventId, createAlbumBody
        )
    }

    private fun createNewAlbumObserver() {
        createNewAlbumViewModel.progressIndicator.observe(viewLifecycleOwner) {
        }
        createNewAlbumViewModel.createNewAlbumResponse.observe(
            viewLifecycleOwner
        ) {
            val success = it.peekContent().success
            val message = it.peekContent().message

            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
            allAlbumApi()
        }

        createNewAlbumViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }


    private fun dpToPx(): Int {
        val r = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            r.displayMetrics
        ).roundToInt()
    }

    fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector!!.onTouchEvent(motionEvent)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f))
            pick_image!!.scaleX = mScaleFactor
            pick_image!!.scaleY = mScaleFactor
            return true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val x = data.clipData!!.itemCount
                    for (i in 0 until x) {
                        uri.add(data.clipData!!.getItemAt(i).uri)
                    }
                    //galleryAdapter!!.notifyDataSetChanged()
                } else if (data.data != null) {
                    val imageUrl = data.data!!.path
                    uri.add(Uri.parse(imageUrl))
                    //galleryAdapter!!.notifyDataSetChanged()
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            // Handle the exception
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeLastImage() {
        try {
            if (uri.isNotEmpty()) {
                uri.removeAt(uri.size - 1)
                //galleryAdapter?.notifyDataSetChanged()
            }
        } catch (e: IndexOutOfBoundsException) {
            // Handle the exception
        }
    }

    companion object {
        private const val Read_Permission = 101
    }
}









